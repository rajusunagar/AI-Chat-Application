package com.deepseek.InsightGPT;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class InsightGptApplication {

        public static void main(String[] args) {
                SpringApplication.run(InsightGptApplication.class, args);
        }
        
        @EventListener(ApplicationStartedEvent.class)
        public void onApplicationStarted() {
                System.out.println("=======================================================");
                System.out.println("   DeepSeek AI Chat Application Started Successfully   ");
                System.out.println("=======================================================");
                System.out.println("Access the chat interface at: http://localhost:8080/");
                System.out.println("API endpoints:");
                System.out.println("  - POST /api/chat/simple - Simple chat with a single message");
                System.out.println("  - POST /api/chat - Advanced chat with multiple messages");
                System.out.println("  - GET /api/chat/health - Health check endpoint");
                System.out.println("=======================================================");
        }
}
