// File: app/src/main/java/com/proapp/obdcodes/network/model/Car.java
package com.eyadalalimi.car.obd2.network.model;

import com.google.gson.annotations.SerializedName;
import androidx.annotation.NonNull;

/**
 * Represents a Car DTO with its properties and timestamps.
 */
public class Car {
    @SerializedName("id")
    private int id;

    @SerializedName("brand_id")
    private int brandId;

    @SerializedName("brand_name")
    @NonNull
    private String brandName;

    @SerializedName("model_id")
    private int modelId;

    @SerializedName("model_name")
    @NonNull
    private String modelName;

    @SerializedName("year")
    @NonNull
    private String year;

    @SerializedName("car_name")
    @NonNull
    private String carName;

    @SerializedName("created_at")
    @NonNull
    private String createdAt;

    @SerializedName("updated_at")
    @NonNull
    private String updatedAt;

    /** Default constructor for serialization. */
    public Car() {
        this.brandName = this.modelName = this.year = this.carName = this.createdAt = this.updatedAt = "";
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBrandId() { return brandId; }
    public void setBrandId(int brandId) { this.brandId = brandId; }

    @NonNull
    public String getBrandName() { return brandName; }
    public void setBrandName(@NonNull String brandName) {
        if (brandName.isEmpty()) throw new IllegalArgumentException("Brand name cannot be empty");
        this.brandName = brandName;
    }

    public int getModelId() { return modelId; }
    public void setModelId(int modelId) { this.modelId = modelId; }

    @NonNull
    public String getModelName() { return modelName; }
    public void setModelName(@NonNull String modelName) {
        if (modelName.isEmpty()) throw new IllegalArgumentException("Model name cannot be empty");
        this.modelName = modelName;
    }

    @NonNull
    public String getYear() { return year; }
    public void setYear(@NonNull String year) {
        if (year.isEmpty()) throw new IllegalArgumentException("Year cannot be empty");
        this.year = year;
    }

    @NonNull
    public String getCarName() { return carName; }
    public void setCarName(@NonNull String carName) {
        if (carName.isEmpty()) throw new IllegalArgumentException("Car name cannot be empty");
        this.carName = carName;
    }

    @NonNull
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(@NonNull String createdAt) { this.createdAt = createdAt; }

    @NonNull
    public String getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(@NonNull String updatedAt) { this.updatedAt = updatedAt; }

    @Override
    public String toString() {
        return brandName + " " + modelName + " (" + year + ")";
    }
}
