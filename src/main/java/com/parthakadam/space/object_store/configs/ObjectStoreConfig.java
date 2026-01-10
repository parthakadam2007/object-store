package com.parthakadam.space.object_store.configs;

import java.io.File;
import java.io.IOError;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

import lombok.Getter;

@Getter
@Service
public class ObjectStoreConfig {
    private final Path uploadDir = Paths.get("D:\\object_store_data")  ; 
    

    public ObjectStoreConfig()throws IOException{
        Files.createDirectories(uploadDir);
    }
}
