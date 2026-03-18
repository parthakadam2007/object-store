package com.parthakadam.space.auth_service.exceptions;


import com.parthakadam.space.auth_service.exceptions.accessTokenExceptions.AccessTokenInfoSavingException;
import com.parthakadam.space.auth_service.exceptions.accessTokenExceptions.AccessTokenValidatingException;
import org.springframework.web.bind.annotation.ControllerAdvice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(AccessTokenInfoSavingException.class)
    public ResponseEntity<ErrorResponse> handleAccessSavingException(AccessTokenInfoSavingException ex) {

        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage()
        );

        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }


    @ExceptionHandler(AccessTokenValidatingException.class)
    public ResponseEntity<ErrorResponse> handleAccessValidationException(AccessTokenValidatingException ex) {
        ErrorResponse error = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}