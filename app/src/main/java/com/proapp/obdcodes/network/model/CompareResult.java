package com.proapp.obdcodes.network.model;

import com.google.gson.annotations.SerializedName;
import com.proapp.obdcodes.network.model.ObdCode;

public class CompareResult {
    @SerializedName("code_1")
    private ObdCode code1;

    @SerializedName("code_2")
    private ObdCode code2;

    public ObdCode getCode1() { return code1; }
    public void setCode1(ObdCode code1) { this.code1 = code1; }

    public ObdCode getCode2() { return code2; }
    public void setCode2(ObdCode code2) { this.code2 = code2; }
}
