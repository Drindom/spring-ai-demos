package com.demo.mcpclient;

import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class McpClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpClientApplication.class, args);
    }

//    @Bean
//    ApplicationRunner printMcpTools(
//            SyncMcpToolCallbackProvider toolCallbackProvider
//    ){
//        return arguments -> {
//            ToolCallback[] toolCallbacks =
//                    toolCallbackProvider.getToolCallbacks();
//
//            System.out.println("发现 MCP 工具数量: " + toolCallbacks.length);
//
//            for (ToolCallback toolCallback : toolCallbacks) {
//                var definition = toolCallback.getToolDefinition();
//                System.out.println("工具名称: " + definition.name());
//                System.out.println("工具描述: " + definition.description());
//                System.out.println("输入结构: " + definition.inputSchema());
//            }
//        };
//    }
}
