package com.eyadalalimi.car.obd2.network.model;

import com.google.gson.annotations.SerializedName;

public class SubscriptionRequest {

    @SerializedName("plan_id")
    private Long planId;

    @SerializedName("purchase_token")
    private String purchaseToken;

    @SerializedName("platform")
    private String platform; // مثل "google_play" أو "ios" أو "stripe"

    @SerializedName("code")
    private String code; // يُستخدم فقط إذا كان التفعيل عبر كود

    // --- Constructors ---

    public SubscriptionRequest() {
    }

    public SubscriptionRequest(Long planId, String purchaseToken, String platform, String code) {
        this.planId = planId;
        this.purchaseToken = purchaseToken;
        this.platform = platform;
        this.code = code;
    }

    public SubscriptionRequest(Long planId, String purchaseToken, String platform) {
        this.planId = planId;
        this.purchaseToken = purchaseToken;
        this.platform = platform;
        this.code = null;
    }

    public SubscriptionRequest(Long planId, String code) {
        this.planId = planId;
        this.code = code;
        this.platform = "manual";
        this.purchaseToken = null;
    }

    // --- Getters & Setters ---

    public Long getPlanId() {
        return planId;
    }

    public void setPlanId(Long planId) {
        this.planId = planId;
    }

    public String getPurchaseToken() {
        return purchaseToken;
    }

    public void setPurchaseToken(String purchaseToken) {
        this.purchaseToken = purchaseToken;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
