package com.UAIC.ISMA.dto;

import java.time.LocalDateTime;

public class RequestDocumentDTO {
    private Long id;
    private String title;
    private String description;
    private String filePath;
    private LocalDateTime uploadedAt;
    private Long accessRequestId;
    private Long uploadedById;

    public RequestDocumentDTO() {
    }

    public RequestDocumentDTO(Long id, String title, String description, String filePath, LocalDateTime uploadedAt, Long accessRequestId, Long uploadedById) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.filePath = filePath;
        this.uploadedAt = uploadedAt;
        this.accessRequestId = accessRequestId;
        this.uploadedById = uploadedById;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public LocalDateTime getUploadedAt() {
        return uploadedAt;
    }

    public void setUploadedAt(LocalDateTime uploadedAt) {
        this.uploadedAt = uploadedAt;
    }

    public Long getAccessRequestId() {
        return accessRequestId;
    }

    public void setAccessRequestId(Long accessRequestId) {
        this.accessRequestId = accessRequestId;
    }

    public Long getUploadedById() {
        return uploadedById;
    }

    public void setUploadedById(Long uploadedById) {
        this.uploadedById = uploadedById;
    }
}
