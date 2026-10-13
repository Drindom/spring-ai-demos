package com.demo.springairag.service;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

@Service
public class RagChatService {
    private final ChatClient chatClient;

    public RagChatService(ChatClient.Builder chatClientBuilder,
                          QuestionAnswerAdvisor questionAnswerAdvisor,
                          ChatMemory chatMemory) {
        chatClient = chatClientBuilder
                .defaultAdvisors(
                        questionAnswerAdvisor,
                        MessageChatMemoryAdvisor.builder(chatMemory).build()
                )
                .defaultSystem(system -> system
                .text(new ClassPathResource("prompts/chat-system.st"))
                .param("role", "知识库问答助手")
                .param("language", "简体中文")
                .param("detailLevel", "具体")
                )
                .build();
    }

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
