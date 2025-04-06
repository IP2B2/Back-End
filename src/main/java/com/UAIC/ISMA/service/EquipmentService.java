package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dao.Equipment;
import com.UAIC.ISMA.dto.EquipmentDTO;
import com.UAIC.ISMA.repository.EquipmentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;

    public EquipmentService(EquipmentRepository equipmentRepository) {
        this.equipmentRepository = equipmentRepository;
    }

    public List<EquipmentDTO> getAllEquipments() {
        return equipmentRepository.findAll().stream()
                        .map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<EquipmentDTO> getEquipmentById(Long id) {
        return equipmentRepository.findById(id).map(this::convertToDTO);
    }

    public EquipmentDTO createEquipment(EquipmentDTO equipmentDTO) {
        Equipment equipment = new Equipment();
        Equipment savedEquipment = equipmentRepository.save(equipment);
        return convertToDTO(savedEquipment);
    }

    public EquipmentDTO updateEquipment(EquipmentDTO equipmentDTO, Long id) {
        Equipment equipment = convertToEntity(equipmentDTO);
        equipment.setId(id);
        Equipment updatedEquipment = equipmentRepository.save(equipment);
        return convertToDTO(updatedEquipment);
    }

    public void deleteEquipment(Long id) {
        equipmentRepository.deleteById(id);
    }

    public EquipmentDTO partialUpdateEquipment(Long id, Map<String, Object> updates) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Equipment not found"));

        updates.forEach((key, value) -> {
            switch (key) {
                case "name" -> equipment.setName((String) value);
                case "photoUrl" -> equipment.setPhotoUrl((String) value);
                case "inventoryNumber" -> equipment.setInventoryNumber((String) value);
                case "acquisitionDate" -> {
                    String acquisitionDate = (String) value;
                    if (!acquisitionDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
                        throw new RuntimeException("Invalid acquisition date format");
                    }
                    equipment.setAcquisitionDate(acquisitionDate);
                }
                case "availabilityStatus" -> equipment.setAvailabilityStatus((String) value);
                case "accesRequirements" -> equipment.setAccessRequirements((String) value);
                default -> throw new RuntimeException("Invalid equipment key");

            }
        });

        Equipment updatedEquipment = equipmentRepository.save(equipment);
        return convertToDTO(updatedEquipment);
    }

    private EquipmentDTO convertToDTO(Equipment equipment) {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setId(equipment.getId());
        dto.setName(equipment.getName());
        dto.setPhotoUrl(equipment.getPhotoUrl());
        dto.setInventoryNumber(equipment.getInventoryNumber());
        dto.setAcquisitionDate(equipment.getAcquisitionDate());
        dto.setAvailabilityStatus(equipment.getAvailabilityStatus());
        dto.setAccessRequirements(equipment.getAccessRequirements());
        return dto;
    }

    private Equipment convertToEntity(EquipmentDTO dto) {
        Equipment e = new Equipment();
        e.setName(dto.getName());
        e.setPhotoUrl(dto.getPhotoUrl());
        e.setInventoryNumber(dto.getInventoryNumber());
        e.setAcquisitionDate(dto.getAcquisitionDate());
        e.setAvailabilityStatus(dto.getAvailabilityStatus());
        e.setAccessRequirements(dto.getAccessRequirements());
        return e;
    }
}
