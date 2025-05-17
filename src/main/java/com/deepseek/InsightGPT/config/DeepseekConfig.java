package com.deepseek.InsightGPT.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class DeepseekConfig {
    
    @Value("${deepseek.api.url:https://api.deepseek.ai/v1/chat/completions}")
    private String apiUrl;
    
    @Value("${deepseek.api.key:your_api_key_here}")
    private String apiKey;
    
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
    
    public String getApiUrl() {
        return apiUrl;
    }
    
    public String getApiKey() {
        return apiKey;
    }
}