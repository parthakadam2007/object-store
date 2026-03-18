package com.parthakadam.space.auth_service.exceptions.accessTokenExceptions;

public class AccessTokenInfoSavingException extends  RuntimeException{
    public  AccessTokenInfoSavingException(String message,Throwable cause){
        super(message,cause);
    }
}
