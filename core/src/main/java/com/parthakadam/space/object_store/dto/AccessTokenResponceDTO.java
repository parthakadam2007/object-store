package com.parthakadam.space.object_store.dto;

import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor

public class AccessTokenResponceDTO {
    private UUID id;

    private UUID accessKeyId;

    private String secretAccessKeyHash;

    private UUID bucketId;

    private OffsetDateTime createdAt;

    private OffsetDateTime expiresAt;

    private Boolean isActive;
}
