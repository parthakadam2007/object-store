package com.parthakadam.space.object_store.configs;

import org.springframework.web.reactive.function.client.WebClient;

public class WebClientAuth {
    public final WebClient client;

    WebClientAuth(WebClient webClient) {
        this.client = webClient;
    }
}
