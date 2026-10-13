package com.demo.springairag.service;

import com.demo.springairag.dto.IngestionResult;
import com.demo.springairag.reader.TextDocumentReader;
import com.demo.springairag.transformer.DocTokenTextSplitter;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DocumentIngestionService {
    private final TextDocumentReader textDocumentReader;
    private final DocTokenTextSplitter docTokenTextSplitter;
    private final VectorStore vectorStore;

    public IngestionResult ingest(String classpathLocation) {
        List<Document> sourceDocument = textDocumentReader.read(classpathLocation);
        List<Document> chunks = docTokenTextSplitter.split(sourceDocument);

        if (!chunks.isEmpty()) {
            vectorStore.add(chunks);
        }
        return new IngestionResult(sourceDocument.size(), chunks.size());
    }
}
