package com.deepseek.InsightGPT.Controller;

import com.deepseek.InsightGPT.model.ChatResponse;
import com.deepseek.InsightGPT.model.Message;
import com.deepseek.InsightGPT.service.AIService;
import com.deepseek.InsightGPT.service.AIServiceFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class ChatControllerTest {

    @Mock
    private AIServiceFactory aiServiceFactory;
    
    @Mock
    private AIService mockAIService;

    @InjectMocks
    private ChatController chatController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(aiServiceFactory.getDefaultService()).thenReturn(mockAIService);
        when(aiServiceFactory.getService(anyString())).thenReturn(mockAIService);
        when(aiServiceFactory.getAvailableServices()).thenReturn(
            Arrays.asList("DeepSeek", "Claude", "ChatGPT", "Gemini")
        );
    }

    @Test
    void simpleChatShouldReturnChatResponse() {
        // Arrange
        Map<String, String> request = new HashMap<>();
        request.put("message", "Hello, AI!");

        ChatResponse mockResponse = new ChatResponse();
        mockResponse.setId("test-id");
        
        ChatResponse.Choice choice = new ChatResponse.Choice();
        choice.setMessage(new Message("assistant", "Hello! How can I help you today?"));
        mockResponse.setChoices(Collections.singletonList(choice));
        
        when(mockAIService.generateChatResponse(anyString())).thenReturn(mockResponse);

        // Act
        ResponseEntity<ChatResponse> response = chatController.simpleChat(request, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("test-id", response.getBody().getId());
        assertEquals("Hello! How can I help you today?", 
                response.getBody().getChoices().get(0).getMessage().getContent());
    }
    
    @Test
    void simpleChatWithServiceParameterShouldUseSpecifiedService() {
        // Arrange
        Map<String, String> request = new HashMap<>();
        request.put("message", "Hello, AI!");

        ChatResponse mockResponse = new ChatResponse();
        mockResponse.setId("test-id");
        
        ChatResponse.Choice choice = new ChatResponse.Choice();
        choice.setMessage(new Message("assistant", "Hello from Claude!"));
        mockResponse.setChoices(Collections.singletonList(choice));
        
        when(mockAIService.generateChatResponse(anyString())).thenReturn(mockResponse);

        // Act
        ResponseEntity<ChatResponse> response = chatController.simpleChat(request, "Claude");

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Hello from Claude!", 
                response.getBody().getChoices().get(0).getMessage().getContent());
    }

    @Test
    void chatShouldReturnChatResponse() {
        // Arrange
        List<Message> messages = List.of(
                new Message("system", "You are a helpful assistant."),
                new Message("user", "Tell me about AI.")
        );

        ChatResponse mockResponse = new ChatResponse();
        mockResponse.setId("test-id");
        
        ChatResponse.Choice choice = new ChatResponse.Choice();
        choice.setMessage(new Message("assistant", "AI is an advanced technology..."));
        mockResponse.setChoices(Collections.singletonList(choice));
        
        when(mockAIService.generateChatResponse(any(List.class))).thenReturn(mockResponse);

        // Act
        ResponseEntity<ChatResponse> response = chatController.chat(messages, null);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("test-id", response.getBody().getId());
        assertEquals("AI is an advanced technology...", 
                response.getBody().getChoices().get(0).getMessage().getContent());
    }
    
    @Test
    void getAvailableServicesShouldReturnListOfServices() {
        // Act
        ResponseEntity<List<String>> response = chatController.getAvailableServices();
        
        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(4, response.getBody().size());
        assertEquals(Arrays.asList("DeepSeek", "Claude", "ChatGPT", "Gemini"), response.getBody());
    }

    @Test
    void healthCheckShouldReturnOk() {
        // Act
        ResponseEntity<String> response = chatController.healthCheck();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("AI Chat Service is running!", response.getBody());
    }
}