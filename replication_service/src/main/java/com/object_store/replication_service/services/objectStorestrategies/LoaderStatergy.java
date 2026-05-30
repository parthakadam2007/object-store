package com.object_store.replication_service.services.objectStorestrategies;

import java.io.FileInputStream;

public interface LoaderStatergy {
    public byte[] loadFile(String path) throws Exception;

}
