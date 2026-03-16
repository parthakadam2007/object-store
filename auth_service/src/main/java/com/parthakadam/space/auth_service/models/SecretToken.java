package com.parthakadam.space.auth_service.models;


import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;

@Entity
@Table(
        name = "secret_token",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "access_key_id")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SecretToken {

    @Id
    @Column(name = "id")
    private UUID id;

    @Column(name = "access_key_id")
    private UUID accessKeyId;

    @Column(name = "secret_access_key_hash")
    private String secretAccessKeyHash;

    @Column(name = "bucket_id")
    private UUID bucketId;

    @CreationTimestamp
    @Column(name = "created_at")
    private OffsetDateTime createdAt;

    @Column(name = "expires_at")
    private OffsetDateTime expiresAt;

    @Column(name = "is_active")
    private Boolean isActive;
}
