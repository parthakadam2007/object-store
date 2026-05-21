/*
 * Copyright (c) 2026 Partha Kadam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.object_store.replication_service.services;

import com.object_store.replication_service.config.ReplicationStorageConfig;
import com.object_store.replication_service.dto.ObjectReplicationMessage;
import com.object_store.replication_service.models.ReplicaEntity;
import com.object_store.replication_service.repository.ReplicaRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReplicationService {

    private final ReplicaRepository replicaRepository;
    private final ReplicationStorageConfig replicationStorageConfig;

    /**
     * Generates a new data path for the replica using sharding strategy.
     * Path structure: /replica_dir/XX/objectId.bin (where XX is first 2 chars of objectId)
     */
    private Path generateReplicaDataPath(String objectId) throws IOException {
        String shard = objectId.substring(0, 2);
        Path shardDir = Paths.get(replicationStorageConfig.getReplicaDir().toString(), shard);
        Files.createDirectories(shardDir);
        return shardDir.resolve(objectId + ".bin");
    }

    /**
     * Copies file from source path to replica location and persists metadata.
     */
    @Transactional
    public ReplicaEntity createReplica(ObjectReplicationMessage message) {
        String objectIdStr = message.getObjectId().toString().replace("-", "");
        Path sourceFilePath = Paths.get(message.getDataPath());
        Path replicaFilePath = null;

        try {
            log.info("Creating replica for object: {}", message.getObjectId());

            // Verify source file exists
            if (!Files.exists(sourceFilePath)) {
                throw new IOException("Source file does not exist: " + sourceFilePath);
            }

            // Generate new replica path
            replicaFilePath = generateReplicaDataPath(objectIdStr);

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

    public long getReplicaCountForObject(UUID objectId) {
        return replicaRepository.countByObjectId(objectId);
    }
}
