package com.aicode.reviewer.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// STAGE 1-2: this class is responsible for talking to the LLM API.
// Start simple: send a string, get a string back. Add prompt-building
// and JSON parsing once that works (see Stage 2-3 in docs/ROADMAP.md).
@Service
public class LlmService {

    @Value("${llm.api.key}")
    private String apiKey;

    @Value("${llm.api.url}")
    private String apiUrl;

    private final RestTemplate restTemplate = new RestTemplate();

    public String callLlm(String prompt) {
        // TODO: build the HTTP request (headers, body) and call apiUrl
        // using restTemplate.postForObject(...) or similar.
        // Return the raw text response for now.
        throw new UnsupportedOperationException("Not implemented yet - Stage 1");
    }
}
