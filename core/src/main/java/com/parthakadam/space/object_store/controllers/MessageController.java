
// package com.parthakadam.space.object_store.controllers;

// import com.parthakadam.space.object_store.services.ReplicationProducerService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.web.bind.annotation.*;

// @RestController
// @RequestMapping("/api/messages")
// @RequiredArgsConstructor
// public class MessageController {

//     private final ReplicationProducerService producerService;

//     @PostMapping
//     public String send(@RequestParam String message) {

//         producerService.sendMessage(message);

//         return "Message sent to Kafka";
//     }
// }