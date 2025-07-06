package com.proapp.obdcodes.network.model;

import com.google.gson.annotations.SerializedName;

public class UserProfileResponse {

    @SerializedName("user")
    private User user;

    public User getUser() {
        return user;
    }
}
