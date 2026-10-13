package com.demo.springaievaluation.dto;

public record RelevancyEvaluationResponse(
        String answer,
        boolean pass,
        float score,
        String feedback
) {
}
