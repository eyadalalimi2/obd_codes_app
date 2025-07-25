package com.eyadalalimi.car.obd2.network.model;

import com.google.gson.annotations.SerializedName;

public class User {

    @SerializedName("id")
    private long id;


    @SerializedName("username")
    private String username;

    @SerializedName("email")
    private String email;

    @SerializedName("phone")
    private String phone;

    @SerializedName("is_admin")
    private boolean isAdmin;

    @SerializedName("role")
    private String role;

    @SerializedName("status")
    private String status;

    @SerializedName("email_verified_at")
    private String emailVerifiedAt;

    @SerializedName("created_at")
    private String createdAt;

    @SerializedName("updated_at")
    private String updatedAt;

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

    @SerializedName("profile_image")
    private String profileImage;
    @SerializedName("subscription")
    private Subscription subscription;

    @SerializedName("saved_codes_count")
    private int savedCodesCount;


    // --- Getters ---

    public long getId() {
        return id;
    }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getPhone() { return phone; }
    public boolean isAdmin() { return isAdmin; }
    public void setIsAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }
    public String getRole() { return role; }
    public String getStatus() { return status; }
    public String getEmailVerifiedAt() { return emailVerifiedAt; }
    public String getCreatedAt() { return createdAt; }
    public String getUpdatedAt() { return updatedAt; }
    public String getJobTitle() { return jobTitle; }
    public String getPackageName() { return packageName; }
    public String getUserMode() { return userMode; }
    public String getCurrentPlan() { return currentPlan; }
    public String getPlanStartDate() { return planStartDate; }
    public String getPlanRenewalDate() { return planRenewalDate; }
    public String getProfileImage() { return profileImage; }
    public Subscription getSubscription() { return subscription; }
    public int getSavedCodesCount()     { return savedCodesCount; }

    public void setProfileImage(String profileImage) { this.profileImage = profileImage; }

    public void setUsername(String username) { this.username = username; }
}
