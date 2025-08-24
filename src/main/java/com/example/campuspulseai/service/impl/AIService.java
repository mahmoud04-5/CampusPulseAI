package com.example.campuspulseai.service.impl;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;

import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class AIService {

    private final ChatClient chatClient;

    public String chat(String prompt) {
        return chatClient
                .prompt(prompt)
                .call()
                .content();
    }

}
