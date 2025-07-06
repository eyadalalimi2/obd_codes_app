package com.proapp.obdcodes.local.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chat_messages")
public class ChatMessageEntity {
    @PrimaryKey(autoGenerate = true)
    private long id;

    private String role;       // user, assistant
    private String content;
    private String timestamp;  // مثل "HH:mm"

    public ChatMessageEntity(String role, String content, String timestamp) {
        this.role = role;
        this.content = content;
        this.timestamp = timestamp;
    }

    // --- Getter & Setter ---
    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getTimestamp() { return timestamp; }
    public void setTimestamp(String timestamp) { this.timestamp = timestamp; }
}
