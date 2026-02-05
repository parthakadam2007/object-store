package com.parthakadam.space.object_store.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class BucketCreateRequestDTO {

    @NotBlank
    @Size(min = 3, max = 63)
    private String name;

    @NotBlank
    @Size(max = 32)
    private String region;
}
