package com.deepseek.InsightGPT.service;

import com.deepseek.InsightGPT.config.DeepseekConfig;
import com.deepseek.InsightGPT.model.ChatResponse;
import com.deepseek.InsightGPT.model.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class DeepseekServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @Mock
    private DeepseekConfig deepseekConfig;

    @InjectMocks
    private DeepseekService deepseekService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(deepseekConfig.getApiUrl()).thenReturn("https://api.deepseek.ai/v1/chat/completions");
        when(deepseekConfig.getApiKey()).thenReturn("test-api-key");
    }

    @Test
    void generateChatResponseWithSingleMessage() {
        // Arrange
        String userMessage = "Hello, DeepSeek!";
        ChatResponse mockResponse = new ChatResponse();
        mockResponse.setId("test-id");
        mockResponse.setObject("chat.completion");
        
        ChatResponse.Choice choice = new ChatResponse.Choice();
        choice.setIndex(0);
        choice.setMessage(new Message("assistant", "Hello! How can I help you today?"));
        mockResponse.setChoices(Collections.singletonList(choice));
        
        when(restTemplate.postForObject(
                eq("https://api.deepseek.ai/v1/chat/completions"),
                any(HttpEntity.class),
                eq(ChatResponse.class)
        )).thenReturn(mockResponse);

        // Act
        ChatResponse result = deepseekService.generateChatResponse(userMessage);

        // Assert
        assertNotNull(result);
        assertEquals("test-id", result.getId());
        assertEquals(1, result.getChoices().size());
        assertEquals("Hello! How can I help you today?", result.getChoices().get(0).getMessage().getContent());
    }

    @Test
    void generateChatResponseWithMultipleMessages() {
        // Arrange
        List<Message> messages = List.of(
                new Message("system", "You are a helpful assistant."),
                new Message("user", "Tell me about DeepSeek AI.")
        );
        
        ChatResponse mockResponse = new ChatResponse();
        mockResponse.setId("test-id");
        mockResponse.setObject("chat.completion");
        
        ChatResponse.Choice choice = new ChatResponse.Choice();
        choice.setIndex(0);
        choice.setMessage(new Message("assistant", "DeepSeek AI is an advanced artificial intelligence model..."));
        mockResponse.setChoices(Collections.singletonList(choice));
        
        when(restTemplate.postForObject(
                eq("https://api.deepseek.ai/v1/chat/completions"),
                any(HttpEntity.class),
                eq(ChatResponse.class)
        )).thenReturn(mockResponse);

        // Act
        ChatResponse result = deepseekService.generateChatResponse(messages);

        // Assert
        assertNotNull(result);
        assertEquals("test-id", result.getId());
        assertEquals(1, result.getChoices().size());
        assertEquals("DeepSeek AI is an advanced artificial intelligence model...", 
                result.getChoices().get(0).getMessage().getContent());
    }
}