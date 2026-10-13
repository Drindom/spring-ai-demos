package com.demo.mcpclient.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
@RequiredArgsConstructor
public class McpChatService {
    private final ChatClient chatClient;

    public Flux<String> chat(String conversationId, String input) {
        return chatClient.prompt()
                .user(input)
                .advisors(advisor -> advisor.param(
                        ChatMemory.CONVERSATION_ID, conversationId
                ))
                .stream()
                .content();
    }
}
