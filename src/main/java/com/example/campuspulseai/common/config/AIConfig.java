package com.example.campuspulseai.common.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AIConfig {
    private final ChatClient.Builder builder;

    public AIConfig(ChatClient.Builder builder) {
        this.builder = builder;
    }

    @Bean
    public ChatClient chatClient() {
        return builder.build();
    }
}
