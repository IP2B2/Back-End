package com.UAIC.ISMA.dto;

public class ResetPasswordRequest {
    private String username; // or email, depending on what you want

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
