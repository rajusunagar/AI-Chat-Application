package com.deepseek.InsightGPT.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

class AIServiceFactoryTest {

    @Mock
    private DeepseekService deepseekService;
    
    @Mock
    private ClaudeService claudeService;
    
    @Mock
    private GeminiService geminiService;
    
    @Mock
    private ChatGPTService chatGPTService;
    
    private AIServiceFactory aiServiceFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        when(deepseekService.getServiceName()).thenReturn("DeepSeek");
        when(claudeService.getServiceName()).thenReturn("Claude");
        when(geminiService.getServiceName()).thenReturn("Gemini");
        when(chatGPTService.getServiceName()).thenReturn("ChatGPT");
        
        aiServiceFactory = new AIServiceFactory(
            deepseekService, claudeService, geminiService, chatGPTService
        );
        
        // Set default service
        ReflectionTestUtils.setField(aiServiceFactory, "defaultServiceName", "DeepSeek");
    }

    @Test
    void getServiceShouldReturnRequestedService() {
        // Act & Assert
        assertEquals(deepseekService, aiServiceFactory.getService("DeepSeek"));
        assertEquals(claudeService, aiServiceFactory.getService("Claude"));
        assertEquals(geminiService, aiServiceFactory.getService("Gemini"));
        assertEquals(chatGPTService, aiServiceFactory.getService("ChatGPT"));
    }
    
    @Test
    void getServiceWithInvalidNameShouldReturnDefaultService() {
        // Act & Assert
        assertEquals(deepseekService, aiServiceFactory.getService("InvalidService"));
    }
    
    @Test
    void getDefaultServiceShouldReturnConfiguredDefaultService() {
        // Act & Assert
        assertEquals(deepseekService, aiServiceFactory.getDefaultService());
        
        // Change default and verify
        ReflectionTestUtils.setField(aiServiceFactory, "defaultServiceName", "Claude");
        assertEquals(claudeService, aiServiceFactory.getDefaultService());
    }
    
    @Test
    void getAvailableServicesShouldReturnAllServices() {
        // Act
        List<String> services = aiServiceFactory.getAvailableServices();
        
        // Assert
        assertEquals(4, services.size());
        assertTrue(services.contains("DeepSeek"));
        assertTrue(services.contains("Claude"));
        assertTrue(services.contains("Gemini"));
        assertTrue(services.contains("ChatGPT"));
    }
}