package com.proapp.obdcodes.network.model;

import com.google.gson.annotations.SerializedName;

public class UpdateProfileRequest {

    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("job_title")
    private String jobTitle;

    @SerializedName("package_name")
    private String packageName;

    @SerializedName("user_mode")
    private String userMode;

    @SerializedName("current_plan")
    private String currentPlan;

    @SerializedName("plan_start_date")
    private String planStartDate;

    @SerializedName("plan_renewal_date")
    private String planRenewalDate;

    public UpdateProfileRequest(String username, String email, String phone, String jobTitle,
                                String packageName, String userMode, String currentPlan,
                                String planStartDate, String planRenewalDate) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.jobTitle = jobTitle;
        this.packageName = packageName;
        this.userMode = userMode;
        this.currentPlan = currentPlan;
        this.planStartDate = planStartDate;
        this.planRenewalDate = planRenewalDate;
    }

    public UpdateProfileRequest(String trim, String trim1, String trim2, String trim3) {
    }

    // Getters and setters إذا لزم الأمر
}
