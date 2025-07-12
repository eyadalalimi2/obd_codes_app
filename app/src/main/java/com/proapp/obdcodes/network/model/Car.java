package com.proapp.obdcodes.network.model;

import com.google.gson.annotations.SerializedName;

public class Car {

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

    public Car() { }

    public int getId() {
        return id;
    }

    public int getBrandId() {
        return brandId;
    }

    public String getBrandName() {
        return brandName;
    }

    public int getModelId() {
        return modelId;
    }

    public String getModelName() {
        return modelName;
    }

    public String getYear() {
        return year;
    }

    public String getCarName() {
        return carName;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public void setModelId(int modelId) {
        this.modelId = modelId;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setCarName(String carName) {
        this.carName = carName;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    @Override
    public String toString() {
        return "Car{" +
                "id=" + id +
                ", brandId=" + brandId +
                ", brandName='" + brandName + '\'' +
                ", modelId=" + modelId +
                ", modelName='" + modelName + '\'' +
                ", year='" + year + '\'' +
                ", carName='" + carName + '\'' +
                ", createdAt='" + createdAt + '\'' +
                ", updatedAt='" + updatedAt + '\'' +
                '}';
    }
}
