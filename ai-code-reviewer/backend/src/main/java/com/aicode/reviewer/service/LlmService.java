package com.aicode.reviewer.service;

import com.aicode.reviewer.dto.ReviewResult;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@Service
public class LlmService {

    @Value("${llm.api.key}")
    private String apiKey;

    @Value("${llm.api.url}")
    private String apiUrl;

    private final PromptBuilder promptBuilder;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    public LlmService(PromptBuilder promptBuilder) {
        this.promptBuilder = promptBuilder;
        this.objectMapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                false);
    }

    public ReviewResult analyzeCode(String code, String language, String action) {
        try {
            String systemInstruction = promptBuilder.buildSystemInstruction(language, action);
            String userContent = promptBuilder.buildUserContent(code, language, action);

            String rawResponse = callLlmWithSystemInstruction(systemInstruction, userContent);
            return parseResult(rawResponse);
        } catch (Exception e) {
            return new ReviewResult(
                    0,
                    "LLM Service Error: " + e.getMessage() + ". Please check your API key and connection.",
                    Collections.emptyList(),
                    "");
        }
    }

    public String callLlm(String prompt) {
        return callLlmWithSystemInstruction("You are a helpful AI software development assistant.", prompt);
    }

    public String callLlmWithSystemInstruction(String systemInstruction, String userContent) {
        try {
            String fullUrl = apiUrl + "?key=" + apiKey;

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Construct System Instruction payload per Google Gemini Documentation
            Map<String, Object> sysPart = new HashMap<>();
            sysPart.put("text", systemInstruction);

            Map<String, Object> sysInstructionObj = new HashMap<>();
            sysInstructionObj.put("parts", Collections.singletonList(sysPart));

            // Construct User Content payload
            Map<String, Object> userPart = new HashMap<>();
            userPart.put("text", userContent);

            Map<String, Object> contentObj = new HashMap<>();
            contentObj.put("parts", Collections.singletonList(userPart));

            Map<String, Object> generationConfig = new HashMap<>();
            generationConfig.put("responseMimeType", "application/json");

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("system_instruction", sysInstructionObj);
            requestBody.put("contents", Collections.singletonList(contentObj));
            requestBody.put("generationConfig", generationConfig);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);
            ResponseEntity<String> response = restTemplate.postForEntity(fullUrl, entity, String.class);

            if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
                JsonNode root = objectMapper.readTree(response.getBody());
                JsonNode candidates = root.path("candidates");
                if (candidates.isArray() && candidates.size() > 0) {
                    JsonNode parts = candidates.get(0).path("content").path("parts");
                    if (parts.isArray() && parts.size() > 0) {
                        return parts.get(0).path("text").asText();
                    }
                }
                return response.getBody();
            } else {
                throw new RuntimeException("LLM API call failed with status: " + response.getStatusCode());
            }
        } catch (org.springframework.web.client.HttpStatusCodeException e) {
            throw new RuntimeException("Gemini API Error (" + e.getStatusCode() + "): " + e.getResponseBodyAsString(),
                    e);
        } catch (Exception e) {
            throw new RuntimeException("Error communicating with Gemini LLM API: " + e.getMessage(), e);
        }
    }

    private ReviewResult parseResult(String rawText) {
        if (rawText == null || rawText.isBlank()) {
            return new ReviewResult(50, "Empty response from AI service.", Collections.emptyList(), "");
        }

        try {
            String cleanedJson = rawText.trim();
            if (cleanedJson.startsWith("```json")) {
                cleanedJson = cleanedJson.substring(7);
            } else if (cleanedJson.startsWith("```")) {
                cleanedJson = cleanedJson.substring(3);
            }
            if (cleanedJson.endsWith("```")) {
                cleanedJson = cleanedJson.substring(0, cleanedJson.length() - 3);
            }
            cleanedJson = cleanedJson.trim();

            return objectMapper.readValue(cleanedJson, ReviewResult.class);
        } catch (Exception e) {
            return new ReviewResult(
                    70,
                    "AI response received. Note: Raw output formatting returned.",
                    Collections.emptyList(),
                    rawText);
        }
    }
}
