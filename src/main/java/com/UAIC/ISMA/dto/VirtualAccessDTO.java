package com.UAIC.ISMA.dto;

import java.time.LocalDateTime;

public class VirtualAccessDTO {
    private Long id;
    private String username;
    private LocalDateTime issuedDate;

    public VirtualAccessDTO() {}

    public VirtualAccessDTO(Long id, String username, LocalDateTime issuedDate) {
        this.id = id;
        this.username = username;
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
}
