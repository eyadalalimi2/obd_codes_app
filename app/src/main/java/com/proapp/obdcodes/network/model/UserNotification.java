package com.proapp.obdcodes.network.model;

import com.google.gson.annotations.SerializedName;

public class UserNotification {
    @SerializedName("id")
    private long id;

    @SerializedName("notification")
    private Notification notification;

    @SerializedName("read_at")
    private String readAt;

    @SerializedName("created_at")
    private String createdAt;

    // getters & setters
    public long getId() { return id; }
    public Notification getNotification() { return notification; }
    public String getReadAt() { return readAt; }
    public String getCreatedAt() { return createdAt; }
}
