// File: com/proapp/obdcodes/network/model/AddCarResponse.java
package com.proapp.obdcodes.network.model;

import com.google.gson.annotations.SerializedName;

public class AddCarResponse {
    @SerializedName("data")
    private Car data;

    public Car getData() { return data; }
    public void setData(Car data) { this.data = data; }

    public static class Car {
        @SerializedName("id")
        private int id;

        @SerializedName("brand_id")
        private int brandId;

        @SerializedName("brand_name")
        private String brandName;

        @SerializedName("model_id")
        private int modelId;

        @SerializedName("model_name")
        private String modelName;

        @SerializedName("year")
        private String year;

        @SerializedName("car_name")
        private String carName;

        @SerializedName("created_at")
        private String createdAt;

        @SerializedName("updated_at")
        private String updatedAt;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public int getBrandId() { return brandId; }
        public void setBrandId(int brandId) { this.brandId = brandId; }

        public String getBrandName() { return brandName; }
        public void setBrandName(String brandName) { this.brandName = brandName; }

        public int getModelId() { return modelId; }
        public void setModelId(int modelId) { this.modelId = modelId; }

        public String getModelName() { return modelName; }
        public void setModelName(String modelName) { this.modelName = modelName; }

        public String getYear() { return year; }
        public void setYear(String year) { this.year = year; }

        public String getCarName() { return carName; }
        public void setCarName(String carName) { this.carName = carName; }

        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }

        public String getUpdatedAt() { return updatedAt; }
        public void setUpdatedAt(String updatedAt) { this.updatedAt = updatedAt; }
    }
}
