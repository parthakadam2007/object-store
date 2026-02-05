package com.parthakadam.space.object_store.services;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;


import com.parthakadam.space.object_store.models.ObjectEntity;

public interface ObjectService {

    public Path generateDataPath(String objectId)throws IOException;

    public ObjectEntity createObjectEntity(String bucketName, String objectKey );

    public ObjectEntity putObject(
        String bucketName,
        String objectKey,
        InputStream data,
        String contentType
    );

    public ObjectEntity getObject(String bucketName,String objectKey);
    // public SyncReplicationStrategy
}
