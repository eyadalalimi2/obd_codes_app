// File: com/proapp/obdcodes/network/model/VerifyStatusResponse.java
package com.proapp.obdcodes.network.model;

import com.google.gson.annotations.SerializedName;

public class VerifyStatusResponse {

    @SerializedName("verified")
    private boolean verified;

    public VerifyStatusResponse(boolean verified) {
        this.verified = verified;
    }

    public boolean isVerified() {
        return verified;
    }

    public void setVerified(boolean verified) {
        this.verified = verified;
    }
}
