package com.UAIC.ISMA.dto;

import java.util.List;

public class RolesResponse {
    private List<String> roles;

    public RolesResponse(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
