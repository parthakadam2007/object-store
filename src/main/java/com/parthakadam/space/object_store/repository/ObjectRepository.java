package com.parthakadam.space.object_store.repository;

import com.parthakadam.space.object_store.models.ObjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ObjectRepository extends JpaRepository<ObjectEntity, UUID> {

    // Check if object exists in a bucket with same key
    boolean existsByBucketIdAndObjectKey(UUID bucketId, String objectKey);

    // Fetch object by bucket + key (used for GET / HEAD)
    Optional<ObjectEntity> findByBucketIdAndObjectKey(UUID bucketId, String objectKey);

    // Delete object by bucket + key
    void deleteByBucketIdAndObjectKey(UUID bucketId, String objectKey);
}
