package com.demo.springairag.transformer;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DocTokenTextSplitter {

    private final TokenTextSplitter tokenTextSplitter;

    public DocTokenTextSplitter() {
        this.tokenTextSplitter = TokenTextSplitter.builder()
                .withChunkSize(500)             // 控制分块大小
                .withMinChunkSizeChars(100)     // 控制分块最小字符数
                .withMinChunkLengthToEmbed(5)   // 控制要嵌入的最小块长度
                .withMaxNumChunks(1000)         // 控制最大分块数量
                .withKeepSeparator(true)        // 控制是否保留分隔符
                .build();
    }

    public List<Document> split(List<Document> documents) {
        return tokenTextSplitter.apply(documents);
    }
}
