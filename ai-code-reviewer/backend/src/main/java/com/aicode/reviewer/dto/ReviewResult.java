package com.aicode.reviewer.dto;

import java.util.List;

// STAGE 3 goal: the LLM's JSON response gets deserialized into this
// object. Add fields here to match your prompt's requested JSON shape.
public class ReviewResult {
    private int score;
    private List<Issue> issues;
    private List<String> improvements;

    // TODO: getters and setters (or convert this to a Java record later)

    public static class Issue {
        private String severity;
        private String category;
        private String description;
        private String suggestion;
        // STAGE 9: add "why" and "risk" fields here for the explainability feature

        // TODO: getters and setters
    }
}
