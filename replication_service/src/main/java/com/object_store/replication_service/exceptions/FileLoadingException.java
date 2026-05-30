package com.object_store.replication_service.exceptions;

public class FileLoadingException extends RuntimeException{

    public FileLoadingException(String message){
        super(message);
    }

    public FileLoadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
