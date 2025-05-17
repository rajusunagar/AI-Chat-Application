package com.deepseek.InsightGPT.Controller;

import com.deepseek.InsightGPT.model.ChatResponse;
import com.deepseek.InsightGPT.model.Message;
import com.deepseek.InsightGPT.service.DeepseekService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/chat")
public class ChatController {

    private final DeepseekService deepseekService;

    @Autowired
    public ChatController(DeepseekService deepseekService) {
        this.deepseekService = deepseekService;
    }

    @PostMapping("/simple")
    public ResponseEntity<ChatResponse> simpleChat(@RequestBody Map<String, String> request) {
        String userMessage = request.get("message");
        if (userMessage == null || userMessage.trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        ChatResponse response = deepseekService.generateChatResponse(userMessage);
        return ResponseEntity.ok(response);
    }
    
    @PostMapping
    public ResponseEntity<ChatResponse> chat(@RequestBody List<Message> messages) {
        if (messages == null || messages.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        ChatResponse response = deepseekService.generateChatResponse(messages);
        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("DeepSeek AI Chat Service is running!");
    }
}
