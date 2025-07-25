// File: app/src/main/java/com/proapp/obdcodes/network/model/UpdateCarsResponse.java
package com.eyadalalimi.car.obd2.network.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class UpdateCarsResponse {
    @SerializedName("data")
    private List<Car> data;

    public List<Car> getData() { return data; }
    public void setData(List<Car> data) { this.data = data; }
}
