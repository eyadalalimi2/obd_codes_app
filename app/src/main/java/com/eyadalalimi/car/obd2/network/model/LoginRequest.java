package com.eyadalalimi.car.obd2.network.model;

public class LoginRequest {
    private final String email;
    private final String password;

    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public String getEmail()      { return email; }
    public String getPassword()   { return password; }
}
