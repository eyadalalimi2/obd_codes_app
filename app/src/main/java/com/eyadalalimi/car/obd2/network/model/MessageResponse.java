// File: com/proapp/obdcodes/network/model/MessageResponse.java
package com.eyadalalimi.car.obd2.network.model;

import com.google.gson.annotations.SerializedName;

public class MessageResponse {

    @SerializedName("message")
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    // getter
    public String getMessage() {
        return message;
    }

    // setter
    public void setMessage(String message) {
        this.message = message;
    }
}
