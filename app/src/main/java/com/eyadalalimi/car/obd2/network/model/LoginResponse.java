package com.eyadalalimi.car.obd2.network.model;

import com.google.gson.annotations.SerializedName;

public class LoginResponse {
    @SerializedName("token")
    private String token;

    @SerializedName("user")
    private User user;

    @SerializedName("message")
    private String message;

    public String getToken() { return token; }
    public User getUser() { return user; }
    public String getMessage() { return message; }

    public boolean isSuccess() {
        return token != null && !token.isEmpty();
    }
}
