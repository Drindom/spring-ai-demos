package com.demo.mcpserver.config;

import com.demo.mcpserver.service.DateTimeService;
import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class McpToolConfig {

    @Bean
    ToolCallbackProvider dateTimeToolCallbackProvider(
            DateTimeService dateTimeService
    ) {
        return MethodToolCallbackProvider.builder()
                .toolObjects(dateTimeService)
                .build();
    }
}
