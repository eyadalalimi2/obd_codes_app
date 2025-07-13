package com.proapp.obdcodes.network.model;

import com.google.gson.annotations.SerializedName;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import java.util.List;

public class Subscription {
    @SerializedName("id")
    private long id;

    @SerializedName("plan")
    private Plan plan;

    @SerializedName("start_at")
    private String startAt;

    @SerializedName("end_at")
    private String endAt;

    @SerializedName("status")
    private String status;

    @SerializedName("features")
    private List<String> features;

    @SerializedName("google_product_id")
    private String googleProductId;

    public long getId() { return id; }
    public Plan getPlan() { return plan; }
    public String getStatus() { return status; }
    public List<String> getFeatures() { return features; }
    public String getStartAt() { return startAt; }
    public String getEndAt() { return endAt; }
    public String getGoogleProductId() { return googleProductId; }

    public String getName() {
        return plan != null ? plan.getName() : null;
    }
    public String getFormattedPrice() {
        return plan != null ? plan.getFormattedPrice() : "-";
    }
    public String getFeaturesText() {
        if (features == null || features.isEmpty()) {
            return "لا توجد مزايا";
        }
        return "- " + String.join("\n- ", features);
    }
    public String getStartDateFormatted() {
        if (startAt == null) return "-";
        try {
            return startAt.split("T")[0];
        } catch (Exception e) {
            return startAt;
        }
    }
    public String getEndDateFormatted() {
        if (endAt == null) return "-";
        try {
            return endAt.split("T")[0];
        } catch (Exception e) {
            return endAt;
        }
    }
    public int getDaysLeft() {
        if (endAt == null) return 0;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            Date endDate = sdf.parse(getEndDateFormatted());
            long diffMs = endDate.getTime() - now.getTime();
            return (int) TimeUnit.DAYS.convert(diffMs, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            return 0;
        }
    }
}
