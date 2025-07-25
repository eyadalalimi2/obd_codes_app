package com.eyadalalimi.car.obd2.network.model;

import java.util.List;

public class ChatRequest {
    private List<ChatMessage> history;
    private String message;

    public ChatRequest() {
        // مطلوب لـ Gson
    }

    public ChatRequest(List<ChatMessage> history, String message) {
        this.history = history;
        this.message = message;
    }

    public List<ChatMessage> getHistory() {
        return history;
    }

    public void setHistory(List<ChatMessage> history) {
        this.history = history;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
