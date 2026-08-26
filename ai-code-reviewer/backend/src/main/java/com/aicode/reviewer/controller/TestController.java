package com.aicode.reviewer.controller;

import org.springframework.web.bind.annotation.*;

// STAGE 1 goal: prove you can send text to an LLM and get text back.
// TODO: inject your LlmService here and call it from this endpoint.
@RestController
@RequestMapping("/api")
public class TestController {

    @PostMapping("/test")
    public String test(@RequestBody String userText) {
        // TODO: replace this with a real call to the LLM service
        return "You said: " + userText;
    }
}
