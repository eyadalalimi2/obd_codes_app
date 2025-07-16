// File: com/proapp/obdcodes/network/model/ForgotPasswordRequest.java
package com.proapp.obdcodes.network.model;

import com.google.gson.annotations.SerializedName;

public class ForgotPasswordRequest {

    @SerializedName("email")
    private String email;

    public ForgotPasswordRequest(String email) {
        this.email = email;
    }

    // getter
    public String getEmail() {
        return email;
    }

    // setter
    public void setEmail(String email) {
        this.email = email;
    }
}
