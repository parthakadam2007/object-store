package com.parthakadam.space.auth_service.DTOs;


import jakarta.persistence.Column;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Setter
@Builder
public class SecretTokenDTO {
    private UUID id;

    private String accessKeyId;

    private String secretAccessKeyHash;

    private UUID bucketId;

    private OffsetDateTime createdAt;

    private OffsetDateTime expiresAt;

    private Boolean isActive;

}
