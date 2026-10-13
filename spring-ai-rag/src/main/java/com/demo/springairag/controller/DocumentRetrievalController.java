package com.demo.springairag.controller;

import com.demo.springairag.dto.RetrievalRequest;
import com.demo.springairag.service.DocumentRetrievalService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rag/retrieval")
@RequiredArgsConstructor
public class DocumentRetrievalController {
    private final DocumentRetrievalService documentRetrievalService;

    @PostMapping("/search")
    public List<Document> search(@Valid @RequestBody RetrievalRequest request) {
        return documentRetrievalService.retrieve(request);
    }
}
