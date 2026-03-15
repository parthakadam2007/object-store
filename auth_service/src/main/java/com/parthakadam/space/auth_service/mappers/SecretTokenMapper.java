package com.parthakadam.space.auth_service.mappers;

import com.parthakadam.space.auth_service.DTOs.SecretTokenDTO;
import com.parthakadam.space.auth_service.models.SecretToken;

public class SecretTokenMapper {

    private  SecretTokenMapper() {};

    public  static SecretTokenDTO toDTO(SecretToken secretToken) {
        return SecretTokenDTO.builder()
                .id(secretToken.getId())
                .accessKeyId(secretToken.getAccessKeyId())
                .secretAccessKeyHash(secretToken.getSecretAccessKeyHash())
                .bucketId(secretToken.getBucketId())
                .createdAt(secretToken.getCreatedAt())
                .expiresAt(secretToken.getExpiresAt())
                .isActive(secretToken.getIsActive())
                .build();
    }
}
