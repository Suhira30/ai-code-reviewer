package com.aicode.reviewer.controller;

import com.aicode.reviewer.dto.ReviewRequest;
import com.aicode.reviewer.dto.ReviewResult;
import com.aicode.reviewer.service.LlmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class ReviewController {

    private final LlmService llmService;

    public ReviewController(LlmService llmService) {
        this.llmService = llmService;
    }

    @PostMapping("/review")
    public ResponseEntity<ReviewResult> reviewCode(@RequestBody(required = false) ReviewRequest request) {
        if (request == null || request.getCode() == null || request.getCode().isBlank()) {
            return ResponseEntity.ok(new ReviewResult(0, "Code snippet cannot be empty. Please paste code to review.", Collections.emptyList(), ""));
        }

        try {
            ReviewResult result = llmService.analyzeCode(request.getCode(), request.getLanguage(), request.getAction());
            return ResponseEntity.ok(result);
        } catch (Exception e) {
            ReviewResult errorResult = new ReviewResult(
                0,
                "Backend Error: " + e.getMessage(),
                Collections.emptyList(),
                ""
            );
            return ResponseEntity.ok(errorResult);
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ReviewResult> handleAllExceptions(Exception ex) {
        ReviewResult errorResult = new ReviewResult(
            0,
            "Server Error: " + ex.getMessage(),
            Collections.emptyList(),
            ""
        );
        return ResponseEntity.ok(errorResult);
    }
}
