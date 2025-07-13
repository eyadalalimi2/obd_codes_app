// File: app/src/main/java/com/proapp/obdcodes/network/model/AddCarResponse.java
package com.proapp.obdcodes.network.model;

import com.google.gson.annotations.SerializedName;
import androidx.annotation.NonNull;

/**
 * Response body for adding a new car.
 */
public class AddCarResponse {
    @SerializedName("data")
    @NonNull
    private Car data;

    public AddCarResponse() { /* for Gson */ }

    @NonNull
    public Car getData() { return data; }
    public void setData(@NonNull Car data) { this.data = data; }

    /**
     * Represents the created Car object.
     */
    public static class Car {
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

        public Car() { }

        public int getId() { return id; }
        public int getBrandId() { return brandId; }
        @NonNull public String getBrandName() { return brandName; }
        public int getModelId() { return modelId; }
        @NonNull public String getModelName() { return modelName; }
        @NonNull public String getYear() { return year; }
        @NonNull public String getCarName() { return carName; }
        @NonNull public String getCreatedAt() { return createdAt; }
        @NonNull public String getUpdatedAt() { return updatedAt; }
    }
}
