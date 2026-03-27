package com.parthakadam.space.object_store.dto;

import com.parthakadam.space.object_store.models.Bucket;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Builder
public class BucketResponseAccessTokenDTO {
    public Bucket bucket;
    public AccessTokenResponceDTO accessTokenResponceDTO;
}
