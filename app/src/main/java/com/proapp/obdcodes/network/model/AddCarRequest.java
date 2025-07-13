// File: app/src/main/java/com/proapp/obdcodes/network/model/AddCarRequest.java
package com.proapp.obdcodes.network.model;

import com.google.gson.annotations.SerializedName;
import androidx.annotation.NonNull;

/**
 * Request body for adding a new car.
 */
public class AddCarRequest {
    @SerializedName("brand_id")
    private int brandId;

    @SerializedName("model_id")
    private int modelId;

    @SerializedName("year")
    @NonNull
    private String year;

    @SerializedName("car_name")
    @NonNull
    private String carName;

    public AddCarRequest(int brandId, int modelId, @NonNull String year, @NonNull String carName) {
        if (year == null || year.isEmpty()) {
            throw new IllegalArgumentException("Year cannot be empty");
        }
        this.brandId = brandId;
        this.modelId = modelId;
        this.year = year;
        // إذا كان carName فارغًا، خزّنه كنص فارغ ولا ترمِ استثناء
        this.carName = carName != null ? carName : "";
    }

    // Default constructor for serialization
    public AddCarRequest() {
        this.year = "";
        this.carName = "";
    }

    public int getBrandId() { return brandId; }
    public void setBrandId(int brandId) { this.brandId = brandId; }

    public int getModelId() { return modelId; }
    public void setModelId(int modelId) { this.modelId = modelId; }

    @NonNull
    public String getYear() { return year; }
    public void setYear(@NonNull String year) {
        if (year == null || year.isEmpty()) {
            throw new IllegalArgumentException("Year cannot be empty");
        }
        this.year = year;
    }

    @NonNull
    public String getCarName() { return carName; }
    public void setCarName(@NonNull String carName) {
        // نقبل carName الفارغ
        this.carName = carName != null ? carName : "";
    }
}
