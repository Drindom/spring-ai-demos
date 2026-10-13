package com.demo.springairag.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Range;

public record RetrievalRequest(
        @NotBlank(message = "query 不能为空")
        String query,

        @NotNull(message = "topK 不能为空")
        @Range(min = 1, max = 20, message = "topK 不能小于 1 或大于 20")
        Integer topK
) {
}
