package com.UAIC.ISMA.dto;

import com.UAIC.ISMA.entity.enums.AvailabilityStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;


import java.time.LocalDate;

public class EquipmentDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @NotBlank(message = "Inventory number is required")
    @Size(max = 50, message = "Inventory number must not exceed 50 characters")
    private String inventoryNumber;

    @NotNull(message = "Availability status must be specified")
    private AvailabilityStatus availabilityStatus;

    @NotNull(message = "Laboratory ID is required")
    private Long laboratoryId;

    @Size(max = 255, message = "Access requirements must not exceed 255 characters")
    private String accessRequirements;

    private String photo;
    private LocalDate acquisitionDate;

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
    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) { this.availabilityStatus = availabilityStatus; }

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
