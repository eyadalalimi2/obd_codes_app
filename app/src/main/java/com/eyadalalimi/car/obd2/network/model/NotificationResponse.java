package com.eyadalalimi.car.obd2.network.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class NotificationResponse {
    @SerializedName("data")
    private List<UserNotification> data;

    public List<UserNotification> getData() {
        return data;
    }
}