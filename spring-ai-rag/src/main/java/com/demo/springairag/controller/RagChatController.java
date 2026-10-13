package com.demo.springairag.controller;

import com.demo.springairag.service.RagChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/chat")
@RequiredArgsConstructor
public class RagChatController {
    private final RagChatService ragChatService;

    @GetMapping(produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> chat(@RequestParam String conversationId,@RequestParam String input) {
        return ragChatService.chat(conversationId, input);
    }
}
