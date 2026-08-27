package com.aicode.reviewer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ReviewResult {

    private int score;
    private String summary;
    private List<Issue> issues;
    private String generatedCode;

    public ReviewResult() {
    }

    public ReviewResult(int score, String summary, List<Issue> issues, String generatedCode) {
        this.score = score;
        this.summary = summary;
        this.issues = issues;
        this.generatedCode = generatedCode;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public List<Issue> getIssues() {
        return issues;
    }

    public void setIssues(List<Issue> issues) {
        this.issues = issues;
    }

    public String getGeneratedCode() {
        return generatedCode;
    }

    public void setGeneratedCode(String generatedCode) {
        this.generatedCode = generatedCode;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Issue {
        private String severity;
        private String category;
        private String title;
        private String description;
        private String why;
        private String risk;
        private String suggestion;
        private Integer lineNumber;

        public Issue() {
        }

        public Issue(String severity, String category, String title, String description, String why, String risk,
                String suggestion, Integer lineNumber) {
            this.severity = severity;
            this.category = category;
            this.title = title;
            this.description = description;
            this.why = why;
            this.risk = risk;
            this.suggestion = suggestion;
            this.lineNumber = lineNumber;
        }

        public String getSeverity() {
            return severity;
        }

        public void setSeverity(String severity) {
            this.severity = severity;
        }

        public String getCategory() {
            return category;
        }

        public void setCategory(String category) {
            this.category = category;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getWhy() {
            return why;
        }

        public void setWhy(String why) {
            this.why = why;
        }

        public String getRisk() {
            return risk;
        }

        public void setRisk(String risk) {
            this.risk = risk;
        }

        public String getSuggestion() {
            return suggestion;
        }

        public void setSuggestion(String suggestion) {
            this.suggestion = suggestion;
        }

        public Integer getLineNumber() {
            return lineNumber;
        }

        public void setLineNumber(Integer lineNumber) {
            this.lineNumber = lineNumber;
        }
    }
}
