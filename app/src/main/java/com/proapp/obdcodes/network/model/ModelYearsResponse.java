// File: app/src/main/java/com/proapp/obdcodes/network/model/ModelYearsResponse.java
package com.proapp.obdcodes.network.model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 * Response containing production years for a model.
 */
public class ModelYearsResponse {
    @SerializedName("data")
    private List<String> years;

    public ModelYearsResponse() { }

    public List<String> getYears() { return years; }
    public void setYears(List<String> years) { this.years = years; }

    @Override
    public String toString() {
        return "ModelYearsResponse{" +
                "years=" + years +
                '}';
    }
}
