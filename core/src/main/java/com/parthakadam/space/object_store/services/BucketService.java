package com.parthakadam.space.object_store.services;

import java.util.List;

import com.parthakadam.space.object_store.models.Bucket;

public interface BucketService {
    public Bucket createBucket(String name , String region);
    public List<Bucket> getBuckets();
    public Bucket getBucketByName(String name);

}
