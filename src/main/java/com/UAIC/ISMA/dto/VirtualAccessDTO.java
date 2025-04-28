package com.UAIC.ISMA.dto;

import java.time.LocalDateTime;

public class VirtualAccessDTO {
    private Long accessRequestId;
    private String username;
    private String password;
    private LocalDateTime issuedDate;

    public VirtualAccessDTO() {
    }

    public VirtualAccessDTO(Long accessRequestId, String username, String password, LocalDateTime issuedDate) {
        this.accessRequestId = accessRequestId;
        this.username = username;
        this.password = password;
        this.issuedDate = issuedDate;
    }

    public Long getAccessRequestId() {
        return accessRequestId;
    }

    public void setAccessRequestId(Long accessRequestId) {
        this.accessRequestId = accessRequestId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDateTime getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDateTime issuedDate) {
        this.issuedDate = issuedDate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
