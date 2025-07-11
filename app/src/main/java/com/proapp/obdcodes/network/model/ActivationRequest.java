package com.proapp.obdcodes.network.model;

import com.google.gson.annotations.SerializedName;

public class ActivationRequest {

    @SerializedName("plan_id")
    private long planId;

    @SerializedName("code")
    private String code;

    public ActivationRequest(long planId, String code) {
        this.planId = planId;
        this.code = code;
    }

    public long getPlanId() {
        return planId;
    }

    public String getCode() {
        return code;
    }
}
