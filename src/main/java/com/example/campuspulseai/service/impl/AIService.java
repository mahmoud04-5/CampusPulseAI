package com.example.campuspulseai.service.impl;

import com.example.campuspulseai.service.IAiService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

@Service
public class AIService implements IAiService {

    private final ChatClient chatClient;

    public AIService(ChatClient.Builder builder) {
        chatClient = builder.build();
    }

    @Override
    public String chat(String prompt) {
        return chatClient
                .prompt(prompt)
                .call()
                .content();
    }

}
