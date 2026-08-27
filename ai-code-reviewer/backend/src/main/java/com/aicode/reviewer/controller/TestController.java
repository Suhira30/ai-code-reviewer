package com.aicode.reviewer.controller;

import com.aicode.reviewer.service.LlmService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class TestController {

    private final LlmService llmService;

    public TestController(LlmService llmService) {
        this.llmService = llmService;
    }

    @PostMapping("/test")
    public String test(@RequestBody String userText) {
        return llmService.callLlm(userText);
    }
}
