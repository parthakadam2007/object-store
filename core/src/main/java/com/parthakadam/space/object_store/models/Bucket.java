package com.parthakadam.space.object_store.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(
    name = "buckets",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = "name")
    }
)
@Getter
@Setter
@Builder
@AllArgsConstructor
public class  Bucket {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(nullable = false, updatable = false)
    private UUID id;

    @Column(nullable = false, length = 63, unique = true)
    private String name;

    @Column(nullable = false, length = 32)
    private String region;

    @Column(name = "created_at", nullable = false)
    private OffsetDateTime createdAt;

    protected Bucket() {
        // JPA only
    }

    public Bucket(UUID id, String name, String region) {
        this.id = id;
        this.name = name;
        this.region = region;
        this.createdAt = OffsetDateTime.now();
    }

    // getters only (immutability recommended)
    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getRegion() { return region; }
    public OffsetDateTime getCreatedAt() { return createdAt; }
}
