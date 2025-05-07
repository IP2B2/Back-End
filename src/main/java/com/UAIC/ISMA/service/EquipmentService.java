package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dao.Equipment;
import com.UAIC.ISMA.dao.Laboratory;
import com.UAIC.ISMA.dao.enums.AvailabilityStatus;
import com.UAIC.ISMA.dto.EquipmentDTO;
import com.UAIC.ISMA.exception.EntityNotFoundException;
import com.UAIC.ISMA.exception.InvalidInputException;
import com.UAIC.ISMA.repository.EquipmentRepository;
import com.UAIC.ISMA.repository.LaboratoryRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final LaboratoryRepository laboratoryRepository;

    public EquipmentService(EquipmentRepository equipmentRepository, LaboratoryRepository laboratoryRepository) {
        this.equipmentRepository = equipmentRepository;
        this.laboratoryRepository = laboratoryRepository;
    }

    public List<EquipmentDTO> getAllEquipments() {
        return equipmentRepository.findAll().stream()
                        .map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<EquipmentDTO> getEquipmentById(Long id) {
        return equipmentRepository.findById(id).map(this::convertToDTO);
    }

    public EquipmentDTO createEquipment(EquipmentDTO equipmentDTO) {
        Equipment equipment = convertToEntity(equipmentDTO);
        Equipment savedEquipment = equipmentRepository.save(equipment);
        return convertToDTO(savedEquipment);
    }

    public EquipmentDTO updateEquipment(EquipmentDTO equipmentDTO, Long id) {
        Equipment existing = equipmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipment not found with id: " + id));

        Equipment equipment = convertToEntity(equipmentDTO);
        equipment.setId(id);

        equipment.setAccessRequests(existing.getAccessRequests());

        Equipment saved = equipmentRepository.save(equipment);
        return convertToDTO(saved);
    }

    public void deleteEquipment(Long id) {
        equipmentRepository.deleteById(id);
    }

    public EquipmentDTO partialUpdateEquipment(Long id, Map<String, Object> updates) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Equipment not found with id: " + id));

        updates.forEach((key, value) -> {
            switch (key) {
                case "name" -> equipment.setName((String) value);
                case "photoUrl" -> equipment.setPhoto((String) value);
                case "inventoryNumber" -> equipment.setInventoryNumber((String) value);
                case "acquisitionDate" -> {
                    try {
                        String dateStr = (String) value;
                        LocalDate date = LocalDate.parse(dateStr);
                        equipment.setAcquisitionDate(date);
                    } catch (DateTimeParseException e) {
                        throw new InvalidInputException("Invalid acquisition date format. Expected yyyy-MM-dd");
                    }
                }
                case "availabilityStatus" -> {
                    try {
                        AvailabilityStatus status = AvailabilityStatus.valueOf(((String) value).toUpperCase());
                        equipment.setAvailabilityStatus(status);
                    } catch (IllegalArgumentException e) {
                        throw new InvalidInputException("Invalid availability status value");
                    }
                }
                default -> throw new InvalidInputException("Invalid update field: " + key);
            }
        });

        Equipment updated = equipmentRepository.save(equipment);
        return convertToDTO(updated);
    }

    private EquipmentDTO convertToDTO(Equipment equipment) {
        EquipmentDTO dto = new EquipmentDTO();
        dto.setId(equipment.getId());
        dto.setName(equipment.getName());
        dto.setPhoto(equipment.getPhoto());
        dto.setInventoryNumber(equipment.getInventoryNumber());
        dto.setAcquisitionDate(equipment.getAcquisitionDate());
        dto.setAvailabilityStatus(equipment.getAvailabilityStatus());
        dto.setAccessRequirements(equipment.getAccessRequirements());

        if(equipment.getLaboratory() != null) {
            dto.setLaboratoryId(equipment.getLaboratory().getId());
        }

        return dto;
    }

    private Equipment convertToEntity(EquipmentDTO dto) {
        Equipment e = new Equipment();
        e.setName(dto.getName());
        e.setPhoto(dto.getPhoto());
        e.setInventoryNumber(dto.getInventoryNumber());
        e.setAcquisitionDate(dto.getAcquisitionDate());
        e.setAvailabilityStatus(dto.getAvailabilityStatus());
        e.setAccessRequirements(dto.getAccessRequirements());


        if(dto.getLaboratoryId() != null) {
            Laboratory lab = laboratoryRepository.findById(dto.getLaboratoryId())
                    .orElseThrow(() -> new EntityNotFoundException("Laboratory not found with id: " + dto.getLaboratoryId()));
            e.setLaboratory(lab);
        }

        return e;
    }
}
