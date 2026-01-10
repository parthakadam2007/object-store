package com.parthakadam.space.object_store.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "objects",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"bucket_id", "object_key"})
    }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ObjectEntity {

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private UUID id;

    @Column(name = "bucket_id", nullable = false)
    private UUID bucketId;

    @Column(name = "object_key", nullable = false)
    private String objectKey;

    @Column(name = "data_path", nullable = false)
    private String dataPath;

    @Column(name = "size_bytes", nullable = false)
    private long sizeBytes;

    @Column(name = "content_type")
    private String contentType;

    @Column(name = "checksum_sha256")
    private String checksumSha256;

    @Column(
        name = "created_at",
        nullable = false,
        updatable = false
    )
    
    private OffsetDateTime createdAt;
}
