package com.deepseek.InsightGPT.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ClaudeConfig {
    
    @Value("${claude.api.url:https://api.anthropic.com/v1/messages}")
    private String apiUrl;
    
    @Value("${claude.api.key:your_api_key_here}")
    private String apiKey;
    
    @Value("${claude.api.model:claude-3-opus-20240229}")
    private String model;
    
    public String getApiUrl() {
        return apiUrl;
    }
    
    public String getApiKey() {
        return apiKey;
    }
    
    public String getModel() {
        return model;
    }
}