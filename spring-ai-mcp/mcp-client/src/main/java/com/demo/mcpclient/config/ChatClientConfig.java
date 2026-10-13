package com.demo.mcpclient.config;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class ChatClientConfig {

    @Bean
    public ChatClient chatClient(
            ChatClient.Builder chatClientBuilder,
            SyncMcpToolCallbackProvider syncMcpToolCallbackProvider,
            ChatMemory chatMemory
    ) {
        return chatClientBuilder
                .defaultSystem("""
                        你是一个能够调用外部工具的助手。
                        当用户询问当前时间时，必须调用提供的时间工具，
                        不允许根据自身知识猜测当前时间。
                        使用简体中文回答。
                        """)
                .defaultTools(syncMcpToolCallbackProvider)
                .defaultAdvisors(MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }
}
