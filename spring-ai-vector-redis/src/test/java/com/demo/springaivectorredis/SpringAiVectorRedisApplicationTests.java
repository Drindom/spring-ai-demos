package com.demo.springaivectorredis;

import org.junit.jupiter.api.Test;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class SpringAiVectorRedisApplicationTests {

    @Autowired
    private EmbeddingModel embeddingModel;

    @Autowired
    private VectorStore vectorStore;

    @Test
    void shouldCreateEmbeddingModelAndVectorStore() {
        assertThat(embeddingModel).isNotNull();
        assertThat(vectorStore).isNotNull();
        assertThat(embeddingModel.dimensions()).isEqualTo(1024);
    }
}
