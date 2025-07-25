package com.eyadalalimi.car.obd2.network.model;

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
