// app/src/main/java/com/proapp/obdcodes/network/model/UpdateProfileRequest.java

package com.proapp.obdcodes.network.model;

import com.google.gson.annotations.SerializedName;

public class UpdateProfileRequest {

    @SerializedName("username")
    private final String username;

    @SerializedName("email")
    private final String email;

    @SerializedName("phone")
    private final String phone;

    @SerializedName("job_title")
    private final String jobTitle;

    public UpdateProfileRequest(String username, String email, String phone, String jobTitle) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.jobTitle = jobTitle;
    }

    // getters (حسب الحاجة)
    public String getUsername() { return username; }
    public String getEmail()    { return email; }
    public String getPhone()    { return phone; }
    public String getJobTitle(){ return jobTitle; }
}
