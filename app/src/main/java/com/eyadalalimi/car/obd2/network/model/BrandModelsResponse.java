// File: app/src/main/java/com/proapp/obdcodes/network/model/BrandModelsResponse.java
package com.eyadalalimi.car.obd2.network.model;

import com.google.gson.annotations.SerializedName;
import androidx.annotation.NonNull;
import java.util.List;

/**
 * Response containing a Brand object and its associated Model list.
 */
public class BrandModelsResponse {
    @SerializedName("brand")
    @NonNull
    private Brand brand;

    @SerializedName("models")
    @NonNull
    private List<Model> models;

    public BrandModelsResponse() { }

    public BrandModelsResponse(@NonNull Brand brand, @NonNull List<Model> models) {
        if (brand == null || models == null)
            throw new IllegalArgumentException("Brand and models must not be null");
        this.brand = brand;
        this.models = models;
    }

    @NonNull
    public Brand getBrand() { return brand; }
    public void setBrand(@NonNull Brand brand) {
        if (brand == null) throw new IllegalArgumentException("Brand cannot be null");
        this.brand = brand;
    }

    @NonNull
    public List<Model> getModels() { return models; }
    public void setModels(@NonNull List<Model> models) {
        if (models == null) throw new IllegalArgumentException("Models list cannot be null");
        this.models = models;
    }
}
