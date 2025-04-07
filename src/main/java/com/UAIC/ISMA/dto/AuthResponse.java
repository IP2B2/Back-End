package com.UAIC.ISMA.dto;

public class AuthResponse {
    String token;

    public AuthResponse(String token) {
        this.token = token;
    }

    public String getToken() {
        return token;
    }
}
