package com.parthakadam.space.object_store.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.UUID;

@Getter
@Builder
public class BucketResponseDTO {

    private UUID id;
    private String name;
    private String region;
    private OffsetDateTime createdAt;

    
}
