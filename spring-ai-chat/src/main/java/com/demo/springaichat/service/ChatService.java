package com.demo.springaichat.service;

import com.demo.springaitools.tool.DateTimeTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class ChatService {
    private final ChatClient chatClient;

    public ChatService(
            ChatClient.Builder chatClientBuilder,
            ChatMemory chatMemory,
            DateTimeTool dateTimeTool) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .defaultTools(dateTimeTool)
                .build();
    }

    public Flux<String> chat(String conversationId, String input) {
        return chatClient.prompt()
                .system(system -> system
                        .text(new ClassPathResource("prompts/chat-system.st"))
                        .param("role", "聊天助手")
                        .param("language", "简体中文")
                        .param("detailLevel", "简洁")
                )
                .user(input)
                .advisors(advisor -> advisor.param(
                        ChatMemory.CONVERSATION_ID, conversationId
                ))
                .stream()
                .content();
    }
}
