package com.object_store.replication_service.services.objectStorestrategies;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.object_store.replication_service.exceptions.FileLoadingException;
import org.slf4j.Logger;
import java.io.IOException;

import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class LocalLoader implements LoaderStatergy {

    private static final Logger logger  = LoggerFactory.getLogger(LocalLoader.class);

    @Override
    public byte[] loadFile(String path) throws Exception  {
      try{
            validateFile(path);
            byte[] buffer = Files.readAllBytes(Paths.get(path));
            return buffer;
        }catch(IOException e){

          logger.error(
                  "Failed to load file from path: {}",
                  path,
                  e
          );
            throw  new FileLoadingException(e.getMessage());
        }
    }

    private  void validateFile(String path) throws Exception{
        if(Files.notExists(Paths.get(path))){
            throw new FileLoadingException("File does not exist");
        }
    }
}
