package com.deepseek.InsightGPT.service;

import com.deepseek.InsightGPT.config.GeminiConfig;
import com.deepseek.InsightGPT.model.ChatResponse;
import com.deepseek.InsightGPT.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Service
public class GeminiService implements AIService {

    private final RestTemplate restTemplate;
    private final GeminiConfig geminiConfig;

    @Autowired
    public GeminiService(RestTemplate restTemplate, GeminiConfig geminiConfig) {
        this.restTemplate = restTemplate;
        this.geminiConfig = geminiConfig;
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
        
        // Build URL with API key
        String url = UriComponentsBuilder.fromHttpUrl(geminiConfig.getApiUrl())
                .queryParam("key", geminiConfig.getApiKey())
                .toUriString();

        // Convert our generic messages to Gemini's format
        Map<String, Object> requestBody = new HashMap<>();
        List<Map<String, Object>> geminiMessages = new ArrayList<>();
        
        for (Message message : messages) {
            Map<String, Object> geminiMessage = new HashMap<>();
            Map<String, String> content = new HashMap<>();
            
            // Convert role to Gemini format
            String role = "user";
            if ("assistant".equals(message.getRole())) {
                role = "model";
            } else if ("system".equals(message.getRole())) {
                role = "user"; // Gemini doesn't have system role, prepend to first user message
            }
            
            content.put("text", message.getContent());
            geminiMessage.put("role", role);
            geminiMessage.put("parts", Collections.singletonList(content));
            geminiMessages.add(geminiMessage);
        }
        
        Map<String, Object> contents = new HashMap<>();
        contents.put("contents", geminiMessages);
        
        HttpEntity<Map<String, Object>> request = new HttpEntity<>(contents, headers);

        // Gemini API returns a different format, so we need to convert it to our ChatResponse format
        Map<String, Object> geminiResponse = restTemplate.postForObject(
                url,
                request,
                Map.class
        );

        // Convert Gemini response to our ChatResponse format
        return convertGeminiResponse(geminiResponse);
    }
    
    @Override
    public String getServiceName() {
        return "Gemini";
    }
    
    private ChatResponse convertGeminiResponse(Map<String, Object> geminiResponse) {
        ChatResponse response = new ChatResponse();
        response.setId(UUID.randomUUID().toString());
        response.setObject("chat.completion");
        response.setCreated(System.currentTimeMillis() / 1000);
        response.setModel("gemini-pro");
        
        String content = "";
        if (geminiResponse.containsKey("candidates")) {
            List<Map<String, Object>> candidates = (List<Map<String, Object>>) geminiResponse.get("candidates");
            if (!candidates.isEmpty()) {
                Map<String, Object> candidate = candidates.get(0);
                List<Map<String, Object>> parts = (List<Map<String, Object>>) 
                        ((Map<String, Object>) candidate.get("content")).get("parts");
                if (!parts.isEmpty()) {
                    content = (String) parts.get(0).get("text");
                }
            }
        }
        
        ChatResponse.Choice choice = new ChatResponse.Choice();
        choice.setIndex(0);
        choice.setMessage(new Message("assistant", content));
        choice.setFinish_reason("stop");
        response.setChoices(Collections.singletonList(choice));
        
        // Add usage information (Gemini doesn't provide this, so we'll estimate)
        ChatResponse.Usage usage = new ChatResponse.Usage();
        usage.setPrompt_tokens(0);
        usage.setCompletion_tokens(0);
        usage.setTotal_tokens(0);
        response.setUsage(usage);
        
        return response;
    }
}