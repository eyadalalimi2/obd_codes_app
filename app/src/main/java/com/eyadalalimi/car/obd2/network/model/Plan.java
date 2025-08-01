package com.eyadalalimi.car.obd2.network.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class Plan {

    @SerializedName("id")
    private long id;

    @SerializedName("name")
    private String name;

    @SerializedName("price")
    private double price;

    @SerializedName("duration_days")
    private int durationDays;

    @SerializedName("description")
    private String description;

    @SerializedName(value = "features_json", alternate = {"features"})
    private List<String> features;

    @SerializedName("google_product_id")
    private String googleProductId;

    public long getId() { return id; }
    public String getName() { return name; }
    public double getPrice() { return price; }
    public int getDurationDays() { return durationDays; }
    public String getDescription() { return description; }
    public List<String> getFeatures() { return features; }
    public String getGoogleProductId() { return googleProductId; }

    public String getFormattedPrice() {
        return price == 0 ? "مجانية" : price + " $";
    }

    public String getFeaturesText() {
        if (features == null || features.isEmpty())
            return "لا توجد ميزات مذكورة";
        return "- " + String.join("\n- ", features);
    }
}
