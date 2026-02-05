package com.object_store.replication_service.strategy;

import java.io.InputStream;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SysncReplicationStrategy implements ReplicationStrategy {
        public int replication_factor = 3;

    public void replicator(
            String bucketName,
            String objectKey,
            InputStream data,
            String contentType){
                //TODO
            }

}
