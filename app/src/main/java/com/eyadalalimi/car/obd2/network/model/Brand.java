// File: app/src/main/java/com/proapp/obdcodes/network/model/Brand.java
package com.eyadalalimi.car.obd2.network.model;

import com.google.gson.annotations.SerializedName;
import androidx.annotation.NonNull;

/**
 * Represents a manufacturer brand.
 */
public class Brand {
    @SerializedName("id")
    private int id;

    @SerializedName("name")
    @NonNull
    private String name;

    public Brand() { this.name = ""; }

    public Brand(int id, @NonNull String name) {
        if (name.isEmpty()) throw new IllegalArgumentException("Brand name cannot be empty");
        this.id = id;
        this.name = name;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    @NonNull
    public String getName() { return name; }
    public void setName(@NonNull String name) {
        if (name.isEmpty()) throw new IllegalArgumentException("Brand name cannot be empty");
        this.name = name;
    }

    @Override
    public String toString() { return name; }
}
