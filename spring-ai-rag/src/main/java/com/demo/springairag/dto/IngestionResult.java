package com.demo.springairag.dto;

public record IngestionResult(
        int sourceDocumentCount,
        int chunkCount
) {
}
