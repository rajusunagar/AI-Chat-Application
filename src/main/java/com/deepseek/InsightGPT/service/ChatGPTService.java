package com.deepseek.InsightGPT.service;

import com.deepseek.InsightGPT.config.ChatGPTConfig;
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
public class ChatGPTService implements AIService {

    private final RestTemplate restTemplate;
    private final ChatGPTConfig chatGPTConfig;

    @Autowired
    public ChatGPTService(RestTemplate restTemplate, ChatGPTConfig chatGPTConfig) {
        this.restTemplate = restTemplate;
        this.chatGPTConfig = chatGPTConfig;
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
        headers.setBearerAuth(chatGPTConfig.getApiKey());

        ChatRequest chatRequest = new ChatRequest();
        chatRequest.setModel(chatGPTConfig.getModel());
        chatRequest.setMessages(messages);

        HttpEntity<ChatRequest> request = new HttpEntity<>(chatRequest, headers);

        return restTemplate.postForObject(
                chatGPTConfig.getApiUrl(),
                request,
                ChatResponse.class
        );
    }
    
    @Override
    public String getServiceName() {
        return "ChatGPT";
    }
}