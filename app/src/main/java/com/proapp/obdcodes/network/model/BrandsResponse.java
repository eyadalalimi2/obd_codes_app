// File: com/proapp/obdcodes/network/model/BrandsResponse.java
package com.proapp.obdcodes.network.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class BrandsResponse {
    @SerializedName("data")
    private List<Brand> data;

    public List<Brand> getData() { return data; }
    public void setData(List<Brand> data) { this.data = data; }

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
}
