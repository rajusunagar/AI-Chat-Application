package com.deepseek.InsightGPT.service;

import com.deepseek.InsightGPT.config.DeepseekConfig;
import com.deepseek.InsightGPT.model.ChatRequest;
import com.deepseek.InsightGPT.model.ChatResponse;
import com.deepseek.InsightGPT.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

@Service
public class DeepseekService implements AIService {

    private final RestTemplate restTemplate;
    private final DeepseekConfig deepseekConfig;

    @Autowired
    public DeepseekService(RestTemplate restTemplate, DeepseekConfig deepseekConfig) {
        this.restTemplate = restTemplate;
        this.deepseekConfig = deepseekConfig;
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
        headers.setBearerAuth(deepseekConfig.getApiKey());

        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setModel("deepseek-chat");
        chatRequest.setMessages(messages);

        HttpEntity<ChatRequest> request = new HttpEntity<>(chatRequest, headers);

        return restTemplate.postForObject(
                deepseekConfig.getApiUrl(),
                request,
                ChatResponse.class
        );
    }
    
    @Override
    public String getServiceName() {
        return "DeepSeek";
    }
}