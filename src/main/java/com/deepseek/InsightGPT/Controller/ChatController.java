package com.deepseek.InsightGPT.Controller;

import com.deepseek.InsightGPT.model.ChatResponse;
import com.deepseek.InsightGPT.model.Message;
import com.deepseek.InsightGPT.service.AIService;
import com.deepseek.InsightGPT.service.AIServiceFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final AIServiceFactory aiServiceFactory;

    @Autowired
    public ChatController(AIServiceFactory aiServiceFactory) {
        this.aiServiceFactory = aiServiceFactory;
    }

    @PostMapping("/simple")
    public ResponseEntity<ChatResponse> simpleChat(
            @RequestBody Map<String, String> request,
            @RequestParam(required = false) String service) {
        
        String userMessage = request.get("message");
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        AIService aiService = service != null ? 
                aiServiceFactory.getService(service) : 
                aiServiceFactory.getDefaultService();
        
        ChatResponse response = aiService.generateChatResponse(userMessage);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping
    public ResponseEntity<ChatResponse> chat(
            @RequestBody List<Message> messages,
            @RequestParam(required = false) String service) {
        
        if (messages == null || messages.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        AIService aiService = service != null ? 
                aiServiceFactory.getService(service) : 
                aiServiceFactory.getDefaultService();
        
        ChatResponse response = aiService.generateChatResponse(messages);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/services")
    public ResponseEntity<List<String>> getAvailableServices() {
        return ResponseEntity.ok(aiServiceFactory.getAvailableServices());
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("AI Chat Service is running!");
    }
}
