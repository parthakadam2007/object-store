package com.parthakadam.space.object_store.services;

import java.util.List;

import com.parthakadam.space.object_store.dto.BucketResponseAccessTokenDTO;
import com.parthakadam.space.object_store.models.Bucket;

public interface BucketService {
    public BucketResponseAccessTokenDTO createBucket(String name , String region);
    public List<Bucket> getBuckets();
    public Bucket getBucketByName(String name);

}
