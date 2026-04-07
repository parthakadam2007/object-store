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
package com.parthakadam.space.auth_service.controllers;


import com.parthakadam.space.auth_service.DTOs.CheckAccessRequestDTO;
import com.parthakadam.space.auth_service.DTOs.SecretTokenDTO;
import com.parthakadam.space.auth_service.models.SecretToken;
import com.parthakadam.space.auth_service.services.accessIDService.AccessIDAuthSerivceImp;
import jakarta.persistence.PostRemove;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.UUID;

@RestController
public class AccessIDAuthController {

    AccessIDAuthSerivceImp accessIDAuthSerivceImp;

    AccessIDAuthController(AccessIDAuthSerivceImp accessIDAuthSerivceImp){
        this.accessIDAuthSerivceImp = accessIDAuthSerivceImp;
    }

    @GetMapping("/checkAccessID")
    public ResponseEntity<Boolean> authenticateAccessID(@RequestBody CheckAccessRequestDTO checkAccessRequestDTO){
        Boolean result  = accessIDAuthSerivceImp.validateAccessID(checkAccessRequestDTO.getAccessKeyId(),checkAccessRequestDTO.getBucketId(), checkAccessRequestDTO.getSecretAccessKeyHash());
        return ResponseEntity.ok(result);
    }

    @PostMapping("/createAccessSecret")
    public ResponseEntity<SecretTokenDTO> createAccessSecret(@RequestBody Map<String, UUID> request) {
        UUID bucketId = request.get("bucketId");
        if(bucketId==null)return ResponseEntity.badRequest().build();

        SecretTokenDTO secretToken = accessIDAuthSerivceImp.createAccessID(bucketId);

        return ResponseEntity.ok(secretToken);
    }
}
