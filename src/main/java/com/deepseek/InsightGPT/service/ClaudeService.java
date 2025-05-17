package com.deepseek.InsightGPT.service;

import com.deepseek.InsightGPT.config.ClaudeConfig;
import com.deepseek.InsightGPT.model.ChatResponse;
import com.deepseek.InsightGPT.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class ClaudeService implements AIService {

    private final RestTemplate restTemplate;
    private final ClaudeConfig claudeConfig;

    @Autowired
    public ClaudeService(RestTemplate restTemplate, ClaudeConfig claudeConfig) {
        this.restTemplate = restTemplate;
        this.claudeConfig = claudeConfig;
    }

    @Override
    public ChatResponse generateChatResponse(String userMessage) {
        return generateChatResponse(Collections.singletonList(
                new Message("user", userMessage)
        ));
    }

    @Override
    public ChatResponse generateChatResponse(List<Message> messages) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-api-key", claudeConfig.getApiKey());
        headers.set("anthropic-version", "2023-06-01");

        // Convert our generic messages to Claude's format
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", claudeConfig.getModel());
        requestBody.put("max_tokens", 2000);
        
        List<Map<String, String>> claudeMessages = new ArrayList<>();
        for (Message message : messages) {
            Map<String, String> claudeMessage = new HashMap<>();
            claudeMessage.put("role", message.getRole());
            claudeMessage.put("content", message.getContent());
            claudeMessages.add(claudeMessage);
        }
        requestBody.put("messages", claudeMessages);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);

        // Claude API returns a different format, so we need to convert it to our ChatResponse format
        Map<String, Object> claudeResponse = restTemplate.postForObject(
                claudeConfig.getApiUrl(),
                request,
                Map.class
        );

        // Convert Claude response to our ChatResponse format
        return convertClaudeResponse(claudeResponse);
    }
    
    @Override
    public String getServiceName() {
        return "Claude";
    }
    
    private ChatResponse convertClaudeResponse(Map<String, Object> claudeResponse) {
        ChatResponse response = new ChatResponse();
        response.setId((String) claudeResponse.get("id"));
        response.setObject("chat.completion");
        response.setCreated(System.currentTimeMillis() / 1000);
        response.setModel(claudeConfig.getModel());
        
        List<Map<String, Object>> contentBlocks = (List<Map<String, Object>>) 
                ((Map<String, Object>) claudeResponse.get("content")).get("blocks");
        String content = "";
        if (contentBlocks != null && !contentBlocks.isEmpty()) {
            content = (String) contentBlocks.get(0).get("text");
        }
        
        ChatResponse.Choice choice = new ChatResponse.Choice();
        choice.setIndex(0);
        choice.setMessage(new Message("assistant", content));
        choice.setFinish_reason("stop");
        response.setChoices(Collections.singletonList(choice));
        
        // Add usage information if available
        if (claudeResponse.containsKey("usage")) {
            Map<String, Integer> usageMap = (Map<String, Integer>) claudeResponse.get("usage");
            ChatResponse.Usage usage = new ChatResponse.Usage();
            usage.setPrompt_tokens(usageMap.getOrDefault("input_tokens", 0));
            usage.setCompletion_tokens(usageMap.getOrDefault("output_tokens", 0));
            usage.setTotal_tokens(usage.getPrompt_tokens() + usage.getCompletion_tokens());
            response.setUsage(usage);
        }
        
        return response;
    }
}