package com.UAIC.ISMA.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LaboratoryDTO {
    private Long id;

    @NotBlank(message = "Name must not be blank")
    @Size(max = 255, message = "Name must be at most 255 characters")
    private String labName;

    @Size(max = 255, message = "Description must be at most 255 characters")
    private String description;

    @NotBlank(message = "Location must not be blank")
    @Size(max = 255, message = "Location must be at most 255 characters")
    private String location;

    private List<Long> equipmentIds;
    private List<Long> labDocumentIds;

    public LaboratoryDTO(Long id, String labName, String description, String location) {
        this.id = id;
        this.labName = labName;
        this.description = description;
        this.location = location;
    }
}
