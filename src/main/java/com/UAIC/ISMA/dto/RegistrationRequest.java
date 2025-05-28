package com.UAIC.ISMA.dto;

import jakarta.validation.constraints.NotBlank;

public class RegistrationRequest {
    private String email;
    @NotBlank
    private String registrationNumber;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRegistrationNumber() {
        return registrationNumber;
    }

    public void setRegistrationNumber(String registrationNumber) {
        this.registrationNumber = registrationNumber;
    }
}