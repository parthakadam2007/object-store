package com.object_store.replication_service.services;

import com.object_store.replication_service.services.objectStorestrategies.LoaderStatergy;
import org.springframework.stereotype.Service;

@Service
public class ObjectLoaderContextService {
    public  LoaderStatergy loaderStatergy;

    public  void setLoaderStatergy(LoaderStatergy loaderStatergy) {
        // Implementation for setting the loader strategy
        this.loaderStatergy =  loaderStatergy;

    }

    public byte[] loadFile(String path) throws  Exception{
        return loaderStatergy.loadFile(path);
    }
}
