package com.parthakadam.space.auth_service.services.accessIDService;

import com.parthakadam.space.auth_service.DTOs.SecretTokenDTO;
import com.parthakadam.space.auth_service.models.SecretToken;

import java.util.UUID;

public interface AccessIDAuthSerivce {
    public SecretTokenDTO createAccessID(UUID objectId);
    public Boolean validateAccessID(UUID accessID , UUID bucketId, String secretAccessKeyHash );
}
