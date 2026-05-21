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
package com.object_store.replication_service.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.object_store.replication_service.dto.ObjectReplicationMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class KafkaReplicationConsumerService {

    private final ReplicationService replicationService;
    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "object-replication", groupId = "replication-service-group")
    public void consume(String message) {
        try {
            log.info("Received replication message: {}", message);

            // Deserialize JSON message to ObjectReplicationMessage
            ObjectReplicationMessage replicationMessage = objectMapper
                    .readValue(message, ObjectReplicationMessage.class);

            // Create replica in the database
            replicationService.createReplica(replicationMessage);

            log.info("Successfully processed replication message for object: {}", 
                    replicationMessage.getObjectId());

        } catch (Exception e) {
            log.error("Failed to process replication message: {}", message, e);
            // TODO: Implement dead-letter queue or retry logic here
        }
    }
}