package com.demo.mcpclient.controller;

import com.demo.mcpclient.service.McpChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class McpChatController {
    private final McpChatService mcpChatService;

    @GetMapping(value = "/chat",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(@RequestParam String conversationId, @RequestParam String input) {
        return mcpChatService.chat(conversationId, input);
    }
}
