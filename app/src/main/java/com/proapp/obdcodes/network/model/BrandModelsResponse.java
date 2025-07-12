// File: com/proapp/obdcodes/network/model/BrandModelsResponse.java
package com.proapp.obdcodes.network.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BrandModelsResponse {
    @SerializedName("brand")
    private Brand brand;

    @SerializedName("models")
    private List<Model> models;

    public Brand getBrand() { return brand; }
    public void setBrand(Brand brand) { this.brand = brand; }

    public List<Model> getModels() { return models; }
    public void setModels(List<Model> models) { this.models = models; }

    public static class Brand {
        @SerializedName("id")
        private int id;

        @SerializedName("name")
        private String name;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }

    public static class Model {
        @SerializedName("id")
        private int id;

        @SerializedName("brand_id")
        private int brandId;

        @SerializedName("name")
        private String name;

        public int getId() { return id; }
        public void setId(int id) { this.id = id; }

        public int getBrandId() { return brandId; }
        public void setBrandId(int brandId) { this.brandId = brandId; }

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
    }
}
