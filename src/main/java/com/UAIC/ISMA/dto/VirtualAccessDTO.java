package com.UAIC.ISMA.dto;

import java.time.LocalDateTime;

public class VirtualAccessDTO {
    private Long id;
    private String username;
    private String password;
    private LocalDateTime issuedDate;
    private Long accessRequestId;

    public VirtualAccessDTO() {
    }

    public VirtualAccessDTO(Long id, String username, String password, LocalDateTime issuedDate) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.issuedDate = issuedDate;
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

    public Long getAccessRequestId() {
        return accessRequestId;
    }

    public void setAccessRequestId(Long accessRequestId) {
        this.accessRequestId = accessRequestId;
    }
}
