package com.object_store.replication_service.executor;


import com.object_store.replication_service.dto.ObjectReplicationMessage;
import com.object_store.replication_service.exceptions.ReplicatonException;
import jakarta.transaction.Transactional;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

public class ObjectReplicationTask implements Callable<String>  {
    private final byte[] buffer ;
    ObjectReplicationMessage objectReplicationMessage;

    public ObjectReplicationTask(ObjectReplicationMessage objectReplicationMessage, byte [] buffer) throws ReplicatonException {
        if(buffer == null ||  buffer.length == 0)
            throw new ReplicatonException("buffer is null or empty");

        this.objectReplicationMessage = objectReplicationMessage;
        this.buffer = buffer;
    }

    @Override
    public String call() throws ReplicatonException, InterruptedException ,IOException {
        Thread.sleep(3000);
        Path copyPath = generateDataPath(objectReplicationMessage.getObjectId().toString(),objectReplicationMessage.getDataPath());


        return "success";
    }


    private static  Path generateDataPath(String objectId , String path) throws IOException {
        String shard = objectId.substring(0, 2);

        //logic for getting the root where objects are stored

        //D:\object_store_data\8a
        String[] paths = path.split("object_store_data");
        String uploadDir = paths[0] + "\\object_store_data";
        Path dir = Paths.get(uploadDir, shard);
        Files.createDirectories(dir);

        return dir.resolve(objectId + ".bin");
    }
}
