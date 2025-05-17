package com.UAIC.ISMA.dto;

import com.UAIC.ISMA.entity.enums.AvailabilityStatus;

import java.time.LocalDate;

public class EquipmentDTO {
    private Long id;
    private String name;
    private String photo;
    private String inventoryNumber;
    private LocalDate acquisitionDate;
    private AvailabilityStatus availabilityStatus;
    private Long laboratoryId;
    private String accessRequirements;

    public EquipmentDTO() {
    }

    public EquipmentDTO(Long id, String name, String photo, String inventoryNumber,
                        LocalDate acquisitionDate, AvailabilityStatus availabilityStatus,
                        Long laboratoryId, String accessRequirements) {
        this.id = id;
        this.name = name;
        this.photo = photo;
        this.inventoryNumber = inventoryNumber;
        this.acquisitionDate = acquisitionDate;
        this.availabilityStatus = availabilityStatus;
        this.laboratoryId = laboratoryId;
        this.accessRequirements = accessRequirements;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public LocalDate getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(LocalDate acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public AvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public Long getLaboratoryId() {
        return laboratoryId;
    }

    public void setLaboratoryId(Long laboratoryId) {
        this.laboratoryId = laboratoryId;
    }

    public String getAccessRequirements() {
        return accessRequirements;
    }

    public void setAccessRequirements(String accessRequirements) {
        this.accessRequirements = accessRequirements;
    }
}
