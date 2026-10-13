package com.demo.springaichat.config;

import com.demo.springaitools.tool.DateTimeTool;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class ToolConfig {

    @Bean
    DateTimeTool dateTimeTool() {
        return new DateTimeTool();
    }
}
