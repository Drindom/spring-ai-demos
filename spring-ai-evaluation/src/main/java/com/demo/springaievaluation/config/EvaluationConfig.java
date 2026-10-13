package com.demo.springaievaluation.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class EvaluationConfig {

    @Bean
    ChatClient chatClient(
            ChatClient.Builder chatClientBuilder,
            ChatMemory chatMemory
    ) {
        return chatClientBuilder
                .defaultSystem("""
                        你是一个问答助手。
                        请严格根据用户提供的上下文回答问题。
                        如果上下文不包含答案，请明确说明无法根据上下文回答。
                        使用简体中文回答。
                        """)
                //.defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @Bean
    RelevancyEvaluator relevancyEvaluator(
            ChatClient.Builder chatClientBuilder
    ) {
        return RelevancyEvaluator.builder()
                .chatClientBuilder(chatClientBuilder)
                .build();
    }

}
