package com.UAIC.ISMA.dto;

import java.time.LocalDateTime;

public class LabDocumentDTO {
    private Long id;
    private String title;
    private String description;
    private String filePath;
    private LocalDateTime updatedAt;
    private Long laboratoryId;

    public LabDocumentDTO() {}

    public LabDocumentDTO(Long id, String title, String description, String filePath,
                          LocalDateTime updatedAt, Long laboratoryId) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.filePath = filePath;
        this.updatedAt = updatedAt;
        this.laboratoryId = laboratoryId;
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

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public Long getLaboratoryId() {
        return laboratoryId;
    }

    public void setLaboratoryId(Long laboratoryId) {
        this.laboratoryId = laboratoryId;
    }
}
