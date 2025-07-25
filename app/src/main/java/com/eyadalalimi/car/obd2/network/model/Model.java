// File: app/src/main/java/com/proapp/obdcodes/network/model/Model.java
package com.eyadalalimi.car.obd2.network.model;

import com.google.gson.annotations.SerializedName;
import androidx.annotation.NonNull;

/**
 * Represents a vehicle model.
 */
public class Model {
    @SerializedName("id")
    private int id;

    @SerializedName("brand_id")
    private int brandId;

    @SerializedName("name")
    @NonNull
    private String name;

    public Model() { this.name = ""; }

    public Model(int id, int brandId, @NonNull String name) {
        if (name.isEmpty()) throw new IllegalArgumentException("Model name cannot be empty");
        this.id = id;
        this.brandId = brandId;
        this.name = name;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getBrandId() { return brandId; }
    public void setBrandId(int brandId) { this.brandId = brandId; }

    @NonNull
    public String getName() { return name; }
    public void setName(@NonNull String name) {
        if (name.isEmpty()) throw new IllegalArgumentException("Model name cannot be empty");
        this.name = name;
    }

    @Override
    public String toString() { return name; }
}
