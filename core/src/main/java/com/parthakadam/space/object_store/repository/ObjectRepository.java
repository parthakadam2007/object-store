/*
 * Copyright (c) 2026 Partha Kadam
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.parthakadam.space.object_store.repository;

import com.parthakadam.space.object_store.models.ObjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
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

    @Query(value = """
            SELECT
                O.id,
                O.bucket_id,
                O.object_key,
                O.data_path,
                O.size_bytes,
                O.content_type,
                O.checksum_sha256,
                O.created_at
            FROM objects O
            JOIN buckets B ON B.id = O.bucket_id
            WHERE B.name = :bucketName
              AND O.object_key = :objectKey
                            """, nativeQuery = true)
    ObjectEntity getObject(String bucketName, String objectKey);
}
