package com.parthakadam.space.auth_service.exceptions.accessTokenExceptions;

public class AccessTokenValidatingException extends RuntimeException{
    public AccessTokenValidatingException(String message, Throwable cause){
        super(message, cause);
    }
}
