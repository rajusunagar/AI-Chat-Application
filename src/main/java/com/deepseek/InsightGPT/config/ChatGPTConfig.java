package com.deepseek.InsightGPT.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ChatGPTConfig {
    
    @Value("${chatgpt.api.url:https://api.openai.com/v1/chat/completions}")
    private String apiUrl;
    
    @Value("${chatgpt.api.key:your_api_key_here}")
    private String apiKey;
    
    @Value("${chatgpt.api.model:gpt-4o}")
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