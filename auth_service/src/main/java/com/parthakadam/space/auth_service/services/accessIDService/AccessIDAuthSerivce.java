package com.parthakadam.space.auth_service.services.accessIDService;

import com.parthakadam.space.auth_service.DTOs.SecretTokenDTO;
import com.parthakadam.space.auth_service.exceptions.accessTokenExceptions.AccessTokenInfoSavingException;
import com.parthakadam.space.auth_service.exceptions.accessTokenExceptions.AccessTokenValidatingException;
import com.parthakadam.space.auth_service.models.SecretToken;

import java.util.UUID;

public interface AccessIDAuthSerivce {
    public SecretTokenDTO createAccessID(UUID objectId)throws AccessTokenInfoSavingException;
    public Boolean validateAccessID(UUID accessID , UUID bucketId, String secretAccessKeyHash ) throws AccessTokenValidatingException;
}
