package com.demo.springairag.config;

import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class RagChatConfig {

    @Bean
    QuestionAnswerAdvisor questionAnswerAdvisor(VectorStore vectorStore) {
        SearchRequest searchRequest = SearchRequest.builder()
                .topK(3)
                .similarityThreshold(0.7)
                .build();

        return QuestionAnswerAdvisor.builder(vectorStore)
                        .searchRequest(searchRequest)
                        .build();
    }
}
