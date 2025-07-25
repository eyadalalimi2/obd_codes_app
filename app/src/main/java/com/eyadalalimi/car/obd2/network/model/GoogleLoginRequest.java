package com.eyadalalimi.car.obd2.network.model;

public class GoogleLoginRequest {
    private final String idToken;

    public GoogleLoginRequest(String idToken) {
        this.idToken = idToken;
    }
    public String getIdToken()    { return idToken; }
}
