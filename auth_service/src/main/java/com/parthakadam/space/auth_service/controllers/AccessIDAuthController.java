package com.parthakadam.space.auth_service.controllers;


import com.parthakadam.space.auth_service.DTOs.SecretTokenDTO;
import com.parthakadam.space.auth_service.models.SecretToken;
import com.parthakadam.space.auth_service.services.accessIDService.AccessIDAuthSerivceImp;
import jakarta.persistence.PostRemove;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
public class AccessIDAuthController {

    AccessIDAuthSerivceImp accessIDAuthSerivceImp;

    AccessIDAuthController(AccessIDAuthSerivceImp accessIDAuthSerivceImp){
        this.accessIDAuthSerivceImp=accessIDAuthSerivceImp;
    }

    @GetMapping("/checkAccessID")
    public ResponseEntity<Boolean> authenticateAccessID(){
            Boolean result = false;
        return ResponseEntity.ok(result);
    }

    @PostMapping("/createAccessSecret")
    public ResponseEntity<SecretTokenDTO>  createAccessSecret(@RequestParam UUID objectId ){
         SecretTokenDTO  secretToken = accessIDAuthSerivceImp.createAccessID(objectId);
         return ResponseEntity.ok(secretToken);
    }


}
