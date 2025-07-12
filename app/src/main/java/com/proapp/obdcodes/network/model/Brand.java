package com.proapp.obdcodes.network.model;

import com.google.gson.annotations.SerializedName;

public class Brand {

    @SerializedName("id")
    private int id;

    @SerializedName("name")
    private String name;

    public Brand() { }

    public Brand(int id, String name) {
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Brand{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
