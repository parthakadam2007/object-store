package com.object_store.replication_service.executor;


import com.object_store.replication_service.dto.ObjectReplicationMessage;
import com.object_store.replication_service.exceptions.ReplicatonException;
import com.object_store.replication_service.models.ReplicaEntity;
import com.object_store.replication_service.repository.ReplicaRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.Callable;

@Slf4j
public class ObjectReplicationTask implements Callable<String>  {
    private final byte[] buffer ;
    ObjectReplicationMessage objectReplicationMessage;
    ReplicaRepository replicaRepository;

    public ObjectReplicationTask(ObjectReplicationMessage objectReplicationMessage, byte [] buffer,ReplicaRepository replicaRepository) throws ReplicatonException {
        if(buffer == null ||  buffer.length == 0)
            throw new ReplicatonException("buffer is null or empty");

        this.objectReplicationMessage = objectReplicationMessage;
        this.buffer = buffer;
        this.replicaRepository = replicaRepository;
    }

    @Override
    public String call() throws ReplicatonException, InterruptedException ,IOException {
        ReplicaEntity replicaEntity =  createReplica(objectReplicationMessage);
        return replicaEntity.toString();
    }



    /**
     * Copies file from source path to replica location and persists metadata.
     */
    @Transactional
    public ReplicaEntity createReplica(ObjectReplicationMessage message) {
        String objectIdStr = message.getObjectId().toString().replace("-", "");
        Path sourceFilePath = Paths.get(message.getDataPath());
        Path replicaFilePath = null;
        Logger log =  LoggerFactory.getLogger(ObjectReplicationTask.class);

        try {
            log.info("Creating replica for object: {}", message.getObjectId());

            // Verify source file exists
            if (!Files.exists(sourceFilePath)) {
                throw new IOException("Source file does not exist: " + sourceFilePath);
            }

            // Generate new replica path
            replicaFilePath = generateReplicaDataPath(objectIdStr , message.getDataPath());

            // Copy file from source to replica location
            log.info("Copying file from {} to {}", sourceFilePath, replicaFilePath);
            Files.copy(sourceFilePath, replicaFilePath);

            // Create and save replica entity with new path
            ReplicaEntity replica = ReplicaEntity.builder()
                    .objectId(message.getObjectId())
                    .dataPath(replicaFilePath.toString())
                    .sizeBytes(message.getSizeBytes())
                    .contentType(message.getContentType())
                    .checksumSha256(message.getChecksumSha256())
                    .createdAt(message.getCreatedAt())
                    .build();

            ReplicaEntity savedReplica = replicaRepository.save(replica);
            log.info("Replica created successfully with ID: {} at path: {}",
                    savedReplica.getId(), replicaFilePath);

            return savedReplica;

        } catch (IOException e) {
            log.error("Failed to copy file for object replica: {}", message.getObjectId(), e);
            // Cleanup: delete replica file if it was created
            if (replicaFilePath != null) {
                try {
                    Files.deleteIfExists(replicaFilePath);
                    log.info("Cleaned up partially created replica file: {}", replicaFilePath);
                } catch (IOException deleteError) {
                    log.error("Failed to cleanup replica file: {}", replicaFilePath, deleteError);
                }
            }
            throw new RuntimeException("Failed to create replica for object: " + message.getObjectId(), e);
        } catch (Exception e) {
            log.error("Failed to create replica for object: {}", message.getObjectId(), e);
            // Cleanup: delete replica file if it was created
            if (replicaFilePath != null) {
                try {
                    Files.deleteIfExists(replicaFilePath);
                } catch (IOException ignored) {
                }
            }
            throw e;
        }
    }



    /**
     *
     * @param objectId
     * @param path
     * @return  dir path of replicated file
     * @throws IOException
     */
    private static  Path generateReplicaDataPath(String objectId , String path) throws IOException {
        String shard = objectId.substring(0, 2);
        UUID copyId = UUID.randomUUID();

        //logic for getting the root where objects are stored

        //D:\object_store_data\8a
        String[] paths = path.split("object_store_data");
        String uploadDir = paths[0] + "\\object_store_data";

        Path dir = Paths.get(uploadDir, shard);

        Files.createDirectories(dir);

        return dir.resolve(copyId.toString().replace("-","") + ".bin");
    }
}
