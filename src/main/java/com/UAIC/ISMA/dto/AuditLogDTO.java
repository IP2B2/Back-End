package com.UAIC.ISMA.dto;

import java.time.LocalDateTime;

public class AuditLogDTO {
    private Long id;
    private String action;
    private String details;
    private LocalDateTime timestamp;
    private Long userId;

    public AuditLogDTO() {
    }

    public AuditLogDTO(Long id, String action, String details, LocalDateTime timestamp, Long userId) {
        this.id = id;
        this.action = action;
        this.details = details;
        this.timestamp = timestamp;
        this.userId = userId;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
