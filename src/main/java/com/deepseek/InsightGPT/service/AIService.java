package com.deepseek.InsightGPT.service;

import com.deepseek.InsightGPT.model.ChatResponse;
import com.deepseek.InsightGPT.model.Message;

import java.util.List;

public interface AIService {
    /**
     * Generate a chat response from a single user message
     * @param userMessage The user's message
     * @return The AI's response
     */
    ChatResponse generateChatResponse(String userMessage);
    
    /**
     * Generate a chat response from a list of messages
     * @param messages List of messages in the conversation
     * @return The AI's response
     */
    ChatResponse generateChatResponse(List<Message> messages);
    
    /**
     * Get the name of the AI service
     * @return The name of the AI service
     */
    String getServiceName();
}