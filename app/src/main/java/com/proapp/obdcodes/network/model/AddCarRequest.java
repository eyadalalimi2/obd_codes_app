// File: com/proapp/obdcodes/network/model/AddCarRequest.java
package com.proapp.obdcodes.network.model;

import com.google.gson.annotations.SerializedName;

public class AddCarRequest {
    @SerializedName("brand_id")
    private int brandId;

    @SerializedName("model_id")
    private int modelId;

    @SerializedName("year")
    private String year;

    @SerializedName("car_name")
    private String carName;

    public AddCarRequest(int brandId, int modelId, String year, String carName) {
        this.brandId = brandId;
        this.modelId = modelId;
        this.year = year;
        this.carName = carName;
    }

    public int getBrandId() { return brandId; }
    public void setBrandId(int brandId) { this.brandId = brandId; }

    public int getModelId() { return modelId; }
    public void setModelId(int modelId) { this.modelId = modelId; }

    public String getYear() { return year; }
    public void setYear(String year) { this.year = year; }

    public String getCarName() { return carName; }
    public void setCarName(String carName) { this.carName = carName; }
}
