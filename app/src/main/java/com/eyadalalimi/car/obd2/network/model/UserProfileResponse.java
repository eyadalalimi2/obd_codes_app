package com.eyadalalimi.car.obd2.network.model;

import com.google.gson.annotations.SerializedName;

public class UserProfileResponse {

    @SerializedName("user")
    private User user;

    public User getUser() {
        return user;
    }
}
