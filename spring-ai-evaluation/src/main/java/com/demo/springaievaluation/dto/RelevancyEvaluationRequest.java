package com.demo.springaievaluation.dto;

import jakarta.validation.constraints.NotBlank;

public record RelevancyEvaluationRequest(
        @NotBlank(message = "question 不能为空")
        String question,

        @NotBlank(message = "context 不能为空")
        String context
) {
}
