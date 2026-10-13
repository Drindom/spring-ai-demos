package com.demo.springairag.controller;

import com.demo.springairag.dto.IngestionResult;
import com.demo.springairag.service.DocumentIngestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/rag/documents")
@RequiredArgsConstructor
public class DocumentIngestionController {
    private final DocumentIngestionService documentIngestionService;

    @PostMapping("/ingest")
    public IngestionResult ingest() {
        return documentIngestionService.ingest(
                "files/spring-ai-rag-zh-practice.txt"
        );
    }
}
