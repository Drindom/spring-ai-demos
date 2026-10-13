package com.demo.springaievaluation.service;

import com.demo.springaievaluation.dto.RelevancyEvaluationRequest;
import com.demo.springaievaluation.dto.RelevancyEvaluationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.evaluation.RelevancyEvaluator;
import org.springframework.ai.document.Document;
import org.springframework.ai.evaluation.EvaluationRequest;
import org.springframework.ai.evaluation.EvaluationResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EvaluationService {
    private final ChatClient chatClient;
    private final RelevancyEvaluator relevancyEvaluator;

    public RelevancyEvaluationResponse evaluate(
            RelevancyEvaluationRequest request
    ) {
        String answer = chatClient.prompt()
                .user(user -> user
                        .text("""
                                上下文：
                                {context}

                                问题：
                                {question}
                                """
                        )
                        .param("context", request.context())
                        .param("question", request.question())
                )
                .call()
                .content();

        Document document = new Document(request.context());

        EvaluationRequest evaluationRequest =
                new EvaluationRequest(
                        request.question(),
                        List.of(document),
                        answer
                );

        EvaluationResponse evaluationResponse =
                relevancyEvaluator.evaluate(evaluationRequest);

        return new RelevancyEvaluationResponse(
                answer,
                evaluationResponse.isPass(),
                evaluationResponse.getScore(),
                evaluationResponse.getFeedback()
        );
    }
}
