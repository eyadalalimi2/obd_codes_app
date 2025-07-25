// File: app/src/main/java/com/proapp/obdcodes/network/model/Delete<Car
package com.eyadalalimi.car.obd2.network.model;

import com.google.gson.annotations.SerializedName;

public class DeleteCarResponse {
    @SerializedName("message")
    private String message;

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
