package com.UAIC.ISMA.dto;

import java.util.List;

public class LaboratoryDTO {
    private Long id;
    private String labName;
    private String description;
    private String location;

    private List<Long> equipmentIds;
    private List<Long> labDocumentIds;

    public LaboratoryDTO() {
    }

    public LaboratoryDTO(Long id, String labName, String description, String location,
                         List<Long> equipmentIds, List<Long> labDocumentIds) {
        this.id = id;
        this.labName = labName;
        this.description = description;
        this.location = location;
        this.equipmentIds = equipmentIds;
        this.labDocumentIds = labDocumentIds;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLabName() {
        return labName;
    }

    public void setLabName(String labName) {
        this.labName = labName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<Long> getEquipmentIds() {
        return equipmentIds;
    }

    public void setEquipmentIds(List<Long> equipmentIds) {
        this.equipmentIds = equipmentIds;
    }

    public List<Long> getLabDocumentIds() {
        return labDocumentIds;
    }

    public void setLabDocumentIds(List<Long> labDocumentIds) {
        this.labDocumentIds = labDocumentIds;
    }
}
