package com.eyadalalimi.car.obd2.network.model;

public class ChatMessage {
    private String role;       // "user" أو "assistant"
    private String content;
    private String timestamp;  // الوقت بصيغة مثل "13:45"

    public ChatMessage() {
        // مطلوب لـ Gson
    }

    /**
     * منشئ بسيط دون طابع زمني.
     */
    public ChatMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }

    /**
     * منشئ كامل مع طابع زمني.
     */
    public ChatMessage(String role, String content, String timestamp) {
        this.role = role;
        this.content = content;
        this.timestamp = timestamp;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
