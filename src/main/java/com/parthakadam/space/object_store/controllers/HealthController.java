package com.parthakadam.space.object_store.controllers;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class HealthController {
    @GetMapping("/")
    public String home() {
        return"healthy";
    }

    @GetMapping("/health")
    public String health() {
        return"ok";
    }
}
