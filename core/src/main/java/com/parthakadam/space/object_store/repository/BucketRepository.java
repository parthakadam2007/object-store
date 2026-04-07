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
    value = "INSERT INTO buckets (name, region,id) VALUES (:name, :region,:id)",
    nativeQuery = true
)
Bucket createBucket(
    @Param("name") String name,
    @Param("region") String region,
    @Param("id") UUID id
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
