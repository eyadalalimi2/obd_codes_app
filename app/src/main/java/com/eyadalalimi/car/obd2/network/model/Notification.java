package com.eyadalalimi.car.obd2.network.model;

import com.google.gson.annotations.SerializedName;

public class Notification {


    @SerializedName("id")
    private long id;

    @SerializedName("title")
    private String title;

    @SerializedName("message")
    private String message;

    @SerializedName("created_at")
    private String createdAt;


    // getters
    public long getId() { return id; }
    public String getTitle() { return title; }
    public String getMessage() { return message; }
    public String getCreatedAt() { return createdAt; }
}
