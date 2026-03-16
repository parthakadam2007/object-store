package com.parthakadam.space.auth_service.DTOs;


import lombok.Getter;

import java.util.UUID;

@Getter
public class CheckAccessRequestDTO {
    private UUID accessKeyId;
    private String secretAccessKeyHash;
}
