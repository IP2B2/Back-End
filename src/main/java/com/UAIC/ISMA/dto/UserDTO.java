package com.UAIC.ISMA.dto;

import com.UAIC.ISMA.dao.enums.RoleName;

public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String status;
    private RoleName roleName;

    public UserDTO() {
    }

    public UserDTO(Long id, String username, String email, String status, RoleName roleName) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.status = status;
        this.roleName = roleName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public RoleName getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleName roleName) {
        this.roleName = roleName;
    }
}
