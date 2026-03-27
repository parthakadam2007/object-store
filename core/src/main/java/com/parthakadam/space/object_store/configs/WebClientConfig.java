package com.parthakadam.space.object_store.configs;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClientAuth webClientAuth() {
        WebClient webClient = WebClient.builder()
                            .baseUrl("http://localhost:8081")
                            .build();

        return new WebClientAuth(webClient);
    }
}
