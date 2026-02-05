package com.parthakadam.space.object_store.object_core;

import org.springframework.stereotype.Service;
import com.parthakadam.space.object_store.services.hardware.HardwareInfo;


@Service
public abstract class ObjectManager {
    // private final BucketsProp  objectStoreProp;
    private final HardwareInfo hardwareInfo;

    public ObjectManager(
        // BucketsProp  objectStoreProp,
        HardwareInfo hardwareInfo
    ){
        // this.objectStoreProp = objectStoreProp;
        this.hardwareInfo = hardwareInfo;
    }
}
