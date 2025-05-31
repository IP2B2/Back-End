package com.UAIC.ISMA.mapper;

import com.UAIC.ISMA.entity.Equipment;
import com.UAIC.ISMA.entity.Laboratory;
import com.UAIC.ISMA.dto.EquipmentDTO;
import com.UAIC.ISMA.exception.EntityNotFoundException;
import com.UAIC.ISMA.repository.LaboratoryRepository;

public class EquipmentMapper {

    public static EquipmentDTO convertToDTO(Equipment equipment) {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setId(equipment.getId());
        dto.setName(equipment.getName());
        dto.setPhoto(equipment.getPhoto());
        dto.setInventoryNumber(equipment.getInventoryNumber());
        dto.setAcquisitionDate(equipment.getAcquisitionDate());
        dto.setAvailabilityStatus(equipment.getAvailabilityStatus());
        dto.setAccessRequirements(equipment.getAccessRequirements());
        dto.setUsage(equipment.getUsage());
        dto.setMaterial(equipment.getMaterial());
        dto.setDescription(equipment.getDescription());
        dto.setIsComplex(equipment.getIsComplex());

        if (equipment.getLaboratory() != null) {
            dto.setLaboratoryId(equipment.getLaboratory().getId());
        }

        return dto;
    }

    public static Equipment convertToEntity(EquipmentDTO dto, LaboratoryRepository laboratoryRepository) {
        Equipment e = new Equipment();
        e.setName(dto.getName());
        e.setPhoto(dto.getPhoto());
        e.setInventoryNumber(dto.getInventoryNumber());
        e.setAcquisitionDate(dto.getAcquisitionDate());
        e.setAvailabilityStatus(dto.getAvailabilityStatus());
        e.setAccessRequirements(dto.getAccessRequirements());
        e.setUsage(dto.getUsage());
        e.setMaterial(dto.getMaterial());
        e.setDescription(dto.getDescription());
        e.setIsComplex(dto.getIsComplex());

        if (dto.getLaboratoryId() != null) {
            Laboratory lab = laboratoryRepository.findById(dto.getLaboratoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Laboratory not found with id: " + dto.getLaboratoryId()));
            e.setLaboratory(lab);
        }

        return e;
    }
}
