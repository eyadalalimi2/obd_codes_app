package com.proapp.obdcodes.network.model;

import com.google.gson.annotations.SerializedName;

public class Subscription {
    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private double price;

    @SerializedName("duration_days")
    private int durationDays;

    // الحقلان الجديدان:
    @SerializedName("start_date")
    private String startDate;

    @SerializedName("expires_at")
    private String expiresAt;

    // getters
    public long getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getDurationDays() { return durationDays; }
    public String getStartDate() { return startDate; }
    public String getExpiresAt() { return expiresAt; }
}
