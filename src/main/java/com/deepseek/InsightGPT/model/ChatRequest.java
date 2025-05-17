package com.deepseek.InsightGPT.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ChatRequest {
    private String model = "deepseek-chat";
    private List<Message> messages;
    private double temperature = 0.7;
    private int max_tokens = 2000;
    private boolean stream = false;
}