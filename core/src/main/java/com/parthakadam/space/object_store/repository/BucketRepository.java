package com.parthakadam.space.object_store.repository;

import org.springframework.stereotype.Repository;

import com.parthakadam.space.object_store.models.Bucket;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

@Repository
public interface BucketRepository extends JpaRepository<Bucket,UUID>{

     boolean existsByName(String name);
@Modifying
@Query(
    value = "INSERT INTO buckets (name, region) VALUES (:name, :region)",
    nativeQuery = true
)
Bucket createBucket(
    @Param("name") String name,
    @Param("region") String region
);

@Modifying
@Query(
    value = "SELECT * FROM buckets",
    nativeQuery = true
)
List<Bucket> getBuckets();

@Query(
    value = "SELECT * FROM buckets where name = :name",
    nativeQuery = true
)
Bucket findByName(String name);
}
