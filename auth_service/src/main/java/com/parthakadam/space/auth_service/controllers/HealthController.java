package com.parthakadam.space.auth_service.controllers;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @GetMapping("/")
    public String home(){
        return "authentication service";
    }

    @GetMapping("/health")
    public String health(){
        return "health";
    }
}
