package com.UAIC.ISMA.mapper;

import com.UAIC.ISMA.entity.Laboratory;
import com.UAIC.ISMA.dto.LaboratoryDTO;


import java.util.stream.Collectors;


public class LaboratoryMapper {

    public static LaboratoryDTO convertToDTO(Laboratory lab) {
        LaboratoryDTO dto = new LaboratoryDTO();
        dto.setId(lab.getId());
        dto.setLabName(lab.getLabName());
        dto.setDescription(lab.getDescription());
        dto.setLocation(lab.getLocation());

        if (lab.getEquipments() != null) {
            dto.setEquipmentIds( lab.getEquipments().stream()
                            .map(e -> e.getId())
                            .collect(Collectors.toList())
            );
        }

        if (lab.getLabDocuments() != null) {
            dto.setLabDocumentIds( lab.getLabDocuments().stream()
                            .map(e -> e.getId())
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }

    public static Laboratory convertToEntity(LaboratoryDTO dto) {
        Laboratory lab = new Laboratory();
        lab.setLabName(dto.getLabName());
        lab.setDescription(dto.getDescription());
        lab.setLocation(dto.getLocation());
        return lab;
    }
}
