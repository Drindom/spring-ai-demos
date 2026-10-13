package com.demo.springaievaluation.controller;

import com.demo.springaievaluation.dto.RelevancyEvaluationRequest;
import com.demo.springaievaluation.dto.RelevancyEvaluationResponse;
import com.demo.springaievaluation.service.EvaluationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/evaluation")
@RequiredArgsConstructor
public class EvaluationController {
    private final EvaluationService evaluationService;

    @GetMapping("/relevancy")
    public RelevancyEvaluationResponse evaluateRelevancy(
            @Valid @RequestBody RelevancyEvaluationRequest request
    ) {
        return evaluationService.evaluate(request);
    }
}
