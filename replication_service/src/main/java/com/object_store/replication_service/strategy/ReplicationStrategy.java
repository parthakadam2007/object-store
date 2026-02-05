package com.object_store.replication_service.strategy;

import java.io.InputStream;

public interface  ReplicationStrategy {
    public int replication_factor = 3;

    public void replicator(
            String bucketName,
            String objectKey,
            InputStream data,
            String contentType);



}
