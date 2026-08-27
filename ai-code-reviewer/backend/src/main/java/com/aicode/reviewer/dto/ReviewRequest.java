package com.aicode.reviewer.dto;

public class ReviewRequest {
    private String code;
    private String language;
    private String action;

    public ReviewRequest() {
    }

    public ReviewRequest(String code, String language, String action) {
        this.code = code;
        this.language = language;
        this.action = action;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
