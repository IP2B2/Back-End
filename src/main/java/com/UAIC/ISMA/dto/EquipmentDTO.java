package com.UAIC.ISMA.dto;

import com.UAIC.ISMA.entity.enums.AvailabilityStatus;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;


import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentDTO {
    private Long id;

    @NotBlank(message = "Name is required")
    @Size(max = 255, message = "Name must not exceed 255 characters")
    private String name;

    @NotBlank(message = "Inventory number is required")
    @Size(max = 255, message = "Inventory number must not exceed 255 characters")
    private String inventoryNumber;

    @NotNull(message = "Availability status must be specified")
    private AvailabilityStatus availabilityStatus;

    @NotNull(message = "Laboratory ID is required")
    private Long laboratoryId;

    @Size(max = 255, message = "Access requirements must not exceed 255 characters")
    private String accessRequirements;

    private String photo;
    private LocalDateTime acquisitionDate;

    @Size(max = 2500, message = "Usage requirements must not exceed 2500 characters")
    private String usage;

    @Size(max = 2500, message = "Material requirements must not exceed 2500 characters")
    private String material;

    @Size(max = 2500, message = "Description must not exceed 2500 characters")
    private String description;
    
    private Boolean isComplex;

    public EquipmentDTO(Long id, String name, String photo, String inventoryNumber, LocalDateTime acquisitionDate
    , AvailabilityStatus availabilityStatus, Long laboratoryId, String accessRequirements) {
        this.id = id;
        this.name = name;
        this.inventoryNumber = inventoryNumber;
        this.availabilityStatus = availabilityStatus;
        this.photo = photo;
        this.laboratoryId = laboratoryId;
        this.accessRequirements = accessRequirements;
        this.acquisitionDate = acquisitionDate;
    }
}
