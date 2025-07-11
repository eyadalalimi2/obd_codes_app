package com.proapp.obdcodes.network.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;
public class Subscription {

    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private double price;

    @SerializedName("duration_days")
    private int durationDays;

    @SerializedName("start_date")
    private String startDate;

    @SerializedName("expires_at")
    private String expiresAt;

    @SerializedName("description")
    private String description;

    @SerializedName("features")
    private List<String> features;

    @SerializedName("google_product_id")
    private String googleProductId;




    // ----------------- Getters -----------------

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getDurationDays() {
        return durationDays;
    }

    public String getStartDate() {
        return startDate;
    }

    public String getExpiresAt() {
        return expiresAt;
    }

    public String getDescription() {
        return description;
    }

    public List<String> getFeatures() {
        return features;
    }

    public String getGoogleProductId() {
        return googleProductId;
    }

    // ----------------- أدوات مساعدة للعرض -----------------

    public String getFormattedPrice() {
        return price == 0 ? "مجانية" : price + " $";
    }

    public String getFeaturesText() {
        if (features == null || features.isEmpty()) return "لا توجد مزايا";
        return "- " + String.join("\n- ", features);
    }

    public String getPlanName() {
        return name != null ? name : "بدون اسم";
    }
    public int getDaysLeft() {
        try {
            if (expiresAt == null) return 0;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date now = new Date();
            Date end = sdf.parse(expiresAt);
            long diff = end.getTime() - now.getTime();
            return (int) TimeUnit.DAYS.convert(diff, TimeUnit.MILLISECONDS);
        } catch (Exception e) {
            return 0;
        }
    }public String getStartDateFormatted() {
        return (startDate != null) ? startDate : "-";
    }

    public String getEndDateFormatted() {
        return (expiresAt != null) ? expiresAt : "-";
    }


}
