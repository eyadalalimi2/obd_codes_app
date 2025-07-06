package com.proapp.obdcodes.network.model;

import java.util.List;

public class ChatResponse {
    private String reply;
    private List<ChatMessage> history;
    private String error;

    public ChatResponse() {
        // مطلوب لـ Gson
    }

    // getters و setters
    public String getReply() {
        return reply;
    }

    public void setReply(String reply) {
        this.reply = reply;
    }

    public List<ChatMessage> getHistory() {
        return history;
    }

    public void setHistory(List<ChatMessage> history) {
        this.history = history;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}
