package com.aicode.reviewer.service;

import org.springframework.stereotype.Component;

@Component
public class PromptBuilder {

    public String buildSystemInstruction(String language, String action) {
        String safeLang = (language != null && !language.isBlank()) ? language : "Java";
        String safeAction = (action != null && !action.isBlank()) ? action : "Review";

        StringBuilder sb = new StringBuilder();
        sb.append("You are an expert senior software engineer and code reviewer.\n");
        sb.append("Target Language: ").append(safeLang).append(". Target Action: ").append(safeAction).append(".\n\n");

        switch (safeAction.toLowerCase()) {
            case "explain":
                sb.append("DIRECTIVE FOR EXPLAIN: Provide a friendly, step-by-step logic breakdown in the 'summary' and 'generatedCode' fields so a student can easily understand what each part does.\n");
                break;
            case "find bugs":
                sb.append("DIRECTIVE FOR FIND BUGS: Focus exclusively on potential runtime crashes, null pointers, logic flaws, and security vulnerabilities.\n");
                break;
            case "generate tests":
                sb.append("DIRECTIVE FOR GENERATE TESTS: Write complete, runnable unit test cases (e.g. JUnit 5 for Java, PyTest for Python, Jest for JavaScript) inside the 'generatedCode' field.\n");
                break;
            default: // Review
                sb.append("DIRECTIVE FOR REVIEW: Provide a balanced evaluation covering correctness, security, efficiency, and code style.\n");
                break;
        }

        sb.append(getLanguageDirective(safeLang)).append("\n\n");

        sb.append("CRITICAL INSTRUCTION: Respond ONLY with a valid, raw JSON object (no markdown code fences outside JSON, no extra prose outside JSON).\n");
        sb.append("The JSON object MUST follow this exact schema:\n");
        sb.append("{\n");
        sb.append("  \"score\": 85,\n");
        sb.append("  \"summary\": \"1-2 sentence high level overview or explanation.\",\n");
        sb.append("  \"issues\": [\n");
        sb.append("    {\n");
        sb.append("      \"severity\": \"Critical\" | \"Warning\" | \"Info\",\n");
        sb.append("      \"category\": \"Bug\" | \"Security\" | \"Performance\" | \"Style\",\n");
        sb.append("      \"title\": \"Short issue title\",\n");
        sb.append("      \"description\": \"Clear technical explanation of what is wrong\",\n");
        sb.append("      \"why\": \"Beginner-friendly explanation of why this matters\",\n");
        sb.append("      \"risk\": \"Consequence if left unfixed\",\n");
        sb.append("      \"suggestion\": \"Corrected code snippet or recommended fix\",\n");
        sb.append("      \"lineNumber\": 1\n");
        sb.append("    }\n");
        sb.append("  ],\n");
        sb.append("  \"generatedCode\": \"Full generated code snippet if action is 'Generate Tests' or 'Explain', otherwise empty string.\"\n");
        sb.append("}\n");

        return sb.toString();
    }

    public String buildUserContent(String code, String language, String action) {
        String safeLang = (language != null && !language.isBlank()) ? language : "Java";
        String safeAction = (action != null && !action.isBlank()) ? action : "Review";

        return "Please perform action '" + safeAction + "' on the following " + safeLang + " code snippet:\n\n```" + safeLang.toLowerCase() + "\n" + code + "\n```";
    }

    private String getLanguageDirective(String language) {
        switch (language.toLowerCase()) {
            case "java":
                return "JAVA GUIDANCE: Check for NullPointerExceptions, resource leak handling (try-with-resources), collection immutability, and thread safety.";
            case "python":
                return "PYTHON GUIDANCE: Check for PEP 8 styling, unhandled exceptions, mutable default arguments, type annotations, and SQL injection risks.";
            case "javascript":
            case "typescript":
                return "JAVASCRIPT GUIDANCE: Check for async/await error handling, loose equality (== vs ===), undefined/null checks, and memory leaks.";
            case "sql":
                return "SQL GUIDANCE: Check for SQL injection risks, missing indexes, SELECT * overuse, and transaction boundary issues.";
            default:
                return "GENERAL GUIDANCE: Evaluate code correctness, edge-case safety, and code readability.";
        }
    }
}
