package com.UAIC.ISMA.service;

import com.UAIC.ISMA.entity.Equipment;
import com.UAIC.ISMA.dto.EquipmentDTO;
import com.UAIC.ISMA.entity.enums.AvailabilityStatus;
import com.UAIC.ISMA.exception.EquipmentNotFoundException;
import com.UAIC.ISMA.exception.InvalidInputException;
import com.UAIC.ISMA.mapper.EquipmentMapper;
import com.UAIC.ISMA.repository.EquipmentRepository;
import com.UAIC.ISMA.repository.LaboratoryRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentService {

    private static final Logger logger = LogManager.getLogger(EquipmentService.class);
    private final EquipmentRepository equipmentRepository;
    private final LaboratoryRepository laboratoryRepository;

    public EquipmentService(EquipmentRepository equipmentRepository,
                            LaboratoryRepository laboratoryRepository) {
        this.equipmentRepository = equipmentRepository;
        this.laboratoryRepository = laboratoryRepository;
    }

    public EquipmentDTO createEquipment(EquipmentDTO dto) {
        logger.info("Creating new equipment: {}", dto.getName());
        Equipment equipment = EquipmentMapper.convertToEntity(dto, laboratoryRepository);
        Equipment saved = equipmentRepository.save(equipment);
        logger.info("Equipment created with ID {}", saved.getId());
        return EquipmentMapper.convertToDTO(saved);
    }

    public EquipmentDTO getEquipmentById(Long id) {
        logger.info("Fetching equipment with ID {}", id);
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Equipment with ID {} not found", id);
                    return new EquipmentNotFoundException(id);
                });
        return EquipmentMapper.convertToDTO(equipment);
    }

    public List<EquipmentDTO> getAllEquipments(Long laboratoryId) {
        logger.info("Fetching all equipment");
        return equipmentRepository.findAll().stream()
                .map(EquipmentMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public EquipmentDTO updateEquipment(EquipmentDTO dto, Long id) {
        logger.info("Updating equipment with ID {}", id);
        Equipment existing = equipmentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Equipment with ID {} not found for update", id);
                    return new EquipmentNotFoundException(id);
                });
        Equipment updated = EquipmentMapper.convertToEntity(dto, laboratoryRepository);
        updated.setId(id);
        updated.setAccessRequests(existing.getAccessRequests());

        Equipment saved = equipmentRepository.save(updated);
        logger.info("Equipment with ID {} updated", id);
        return EquipmentMapper.convertToDTO(saved);
    }

    public void deleteEquipment(Long id) {
        logger.info("Deleting equipment with ID {}", id);
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Equipment with ID {} not found for deletion", id);
                    return new EquipmentNotFoundException(id);
                });
        equipmentRepository.delete(equipment);
        logger.info("Equipment with ID {} deleted", id);
    }

    public Page<EquipmentDTO> searchEquipment(String name, String status, Long labId, Pageable pageable) {
        logger.info("Searching equipment: name={}, status={}, labId={}", name, status, labId);
        AvailabilityStatus parsedStatus = null;
        if (status != null) {
            try {
                parsedStatus = AvailabilityStatus.valueOf(status.toUpperCase());
            } catch (IllegalArgumentException e) {
                logger.error("Invalid availability status: {}", status);
                throw new InvalidInputException("Invalid availability status: " + status);
            }
        }

        Page<EquipmentDTO> results = equipmentRepository.searchByNameStatusAndLabId(name, parsedStatus, labId, pageable);
        logger.info("Search returned {} results", results.getTotalElements());
        return results;
    }
}
