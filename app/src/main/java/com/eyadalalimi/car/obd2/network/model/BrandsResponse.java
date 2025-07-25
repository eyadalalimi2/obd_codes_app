// File: app/src/main/java/com/proapp/obdcodes/network/model/BrandsResponse.java
package com.eyadalalimi.car.obd2.network.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Response containing list of available brands.
 */
public class BrandsResponse {
    @SerializedName("data")
    private List<Brand> data;

    public BrandsResponse() { }

    public List<Brand> getData() { return data; }
    public void setData(List<Brand> data) { this.data = data; }
}
