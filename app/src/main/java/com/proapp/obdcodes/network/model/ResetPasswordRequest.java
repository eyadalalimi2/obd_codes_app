// File: com/proapp/obdcodes/network/model/ResetPasswordRequest.java
package com.proapp.obdcodes.network.model;

import com.google.gson.annotations.SerializedName;

public class ResetPasswordRequest {

    @SerializedName("email")
    private String email;

    @SerializedName("token")
    private String token;

    @SerializedName("password")
    private String password;

    @SerializedName("password_confirmation")
    private String passwordConfirmation;

    public ResetPasswordRequest(String email, String token, String password, String passwordConfirmation) {
        this.email = email;
        this.token = token;
        this.password = password;
        this.passwordConfirmation = passwordConfirmation;
    }

    // getters
    public String getEmail() {
        return email;
    }
    public String getToken() {
        return token;
    }
    public String getPassword() {
        return password;
    }
    public String getPasswordConfirmation() {
        return passwordConfirmation;
    }

    // setters
    public void setEmail(String email) {
        this.email = email;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setPasswordConfirmation(String passwordConfirmation) {
        this.passwordConfirmation = passwordConfirmation;
    }
}
