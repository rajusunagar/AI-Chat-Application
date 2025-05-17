package com.deepseek.InsightGPT.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Message {
    private String role; // "system", "user", or "assistant"
    private String content;
}