package com.parthakadam.space.object_store.configs;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class KafkaTopicConfig {

    @Bean
    public NewTopic objectReplicationTopic() {
        return new NewTopic("object-replication", 1, (short) 1);
    }

    @Bean
    public NewTopic legacyTopic() {
        return new NewTopic("my-topic", 1, (short) 1);
    }
}