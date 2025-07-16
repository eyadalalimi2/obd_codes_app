package com.proapp.obdcodes.network.model;

import com.google.gson.annotations.SerializedName;

public class VerifyStatusResponse {
    @SerializedName("verified")
    private boolean verified;

    @SerializedName("email_verified_at")
    private String emailVerifiedAt;

    public boolean isVerified() {
        return verified;
    }

    public String getEmailVerifiedAt() {
        return emailVerifiedAt;
    }
}
