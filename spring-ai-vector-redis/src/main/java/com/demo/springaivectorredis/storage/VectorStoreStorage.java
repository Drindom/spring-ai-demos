package com.demo.springaivectorredis.storage;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Component
public class VectorStoreStorage {
    private final VectorStore vectorStore;

    public VectorStoreStorage(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public void store(List<Document> documents) {
        if (documents == null || documents.isEmpty())
            return;
        vectorStore.add(documents);
    }

    public void delete(Set<String> ids) {
        vectorStore.delete(new ArrayList<>(ids));
    }

    public List<Document> similaritySearch(String query) {
        if (query == null || query.isBlank()) {
            throw new IllegalArgumentException("query 不能为空");
        }

        SearchRequest request = SearchRequest.builder()
                .query(query)
                .topK(5)
                .similarityThresholdAll()
                .build();
        return vectorStore.similaritySearch(request);
    }
}
