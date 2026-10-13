package com.demo.springairag.service;

import com.demo.springairag.dto.RetrievalRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentRetrievalService {
    private final VectorStore vectorStore;

    public List<Document> retrieve(RetrievalRequest request) {
        SearchRequest searchRequest = SearchRequest.builder()
                .query(request.query())
                .topK(request.topK())
                .similarityThresholdAll()
                .build();
        return vectorStore.similaritySearch(searchRequest);
    }

}
