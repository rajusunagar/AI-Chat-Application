package com.deepseek.InsightGPT.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class AIServiceFactory {

    private final Map<String, AIService> services = new HashMap<>();
    
    @Value("${default.ai.service:DeepSeek}")
    private String defaultServiceName;

    @Autowired
    public AIServiceFactory(DeepseekService deepseekService, 
                           ClaudeService claudeService,
                           GeminiService geminiService,
                           ChatGPTService chatGPTService) {
        services.put(deepseekService.getServiceName(), deepseekService);
        services.put(claudeService.getServiceName(), claudeService);
        services.put(geminiService.getServiceName(), geminiService);
        services.put(chatGPTService.getServiceName(), chatGPTService);
    }

    public AIService getService(String serviceName) {
        return services.getOrDefault(serviceName, services.get(defaultServiceName));
    }
    
    public AIService getDefaultService() {
        return services.get(defaultServiceName);
    }
    
    public List<String> getAvailableServices() {
        return services.keySet().stream().sorted().toList();
    }
}