package com.proapp.obdcodes.network.model;

import com.google.gson.annotations.SerializedName;

public class Model {

    @SerializedName("id")
    private int id;

    @SerializedName("brand_id")
    private int brandId;

    @SerializedName("name")
    private String name;

    public Model() { }

    public Model(int id, int brandId, String name) {
        this.id = id;
        this.brandId = brandId;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getBrandId() {
        return brandId;
    }

    public void setBrandId(int brandId) {
        this.brandId = brandId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Model{" +
                "id=" + id +
                ", brandId=" + brandId +
                ", name='" + name + '\'' +
                '}';
    }
}
