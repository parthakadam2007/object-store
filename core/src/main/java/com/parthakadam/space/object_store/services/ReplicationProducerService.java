
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

package com.parthakadam.space.object_store.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parthakadam.space.object_store.dto.ObjectReplicationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReplicationProducerService {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    private static final String TOPIC = "object-replication";

    public void sendReplicationMessage(ObjectReplicationMessage message) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            String key = message.getObjectId().toString();

            log.info("Sending replication message for object: {}", message.getObjectId());

            CompletableFuture<?> future = kafkaTemplate.send(TOPIC, key, jsonMessage);

            future.whenComplete((result, ex) -> {
                if (ex == null) {
                    log.info("Replication message sent successfully for object: {}", 
                            message.getObjectId());
                } else {
                    log.error("Failed to send replication message for object: {}", 
                            message.getObjectId(), ex);
                }
            });
        } catch (Exception e) {
            log.error("Error serializing replication message for object: {}", 
                    message.getObjectId(), e);
            throw new RuntimeException("Failed to send replication message", e);
        }
    }
}