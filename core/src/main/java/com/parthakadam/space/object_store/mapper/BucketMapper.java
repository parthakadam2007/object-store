package com.parthakadam.space.object_store.mapper;

import com.parthakadam.space.object_store.dto.BucketResponseDTO;
import com.parthakadam.space.object_store.models.Bucket;

public class BucketMapper {

    private BucketMapper() {}

    public static BucketResponseDTO toDTO(Bucket bucket) {
        return BucketResponseDTO.builder()
                .id(bucket.getId())
                .name(bucket.getName())
                .region(bucket.getRegion())
                .createdAt(bucket.getCreatedAt())
                .build();
    }
}
