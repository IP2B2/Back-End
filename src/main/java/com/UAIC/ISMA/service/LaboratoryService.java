package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dto.EquipmentDTO;
import com.UAIC.ISMA.entity.Laboratory;
import com.UAIC.ISMA.dto.LaboratoryDTO;
import com.UAIC.ISMA.exception.InvalidInputException;
import com.UAIC.ISMA.exception.LaboratoryNotFoundException;
import com.UAIC.ISMA.mapper.EquipmentMapper;
import com.UAIC.ISMA.mapper.LaboratoryMapper;
import com.UAIC.ISMA.repository.LaboratoryRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
public class LaboratoryService {

    private static final Logger logger = LogManager.getLogger(LaboratoryService.class);

    public final LaboratoryRepository laboratoryRepository;

    public LaboratoryService(LaboratoryRepository laboratoryRepository) {
        this.laboratoryRepository = laboratoryRepository;
    }

    public List<LaboratoryDTO> getAlLaboratories() {
        logger.info("Fetching all laboratories...");

        return laboratoryRepository.findAll().stream()
                .map(LaboratoryMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public LaboratoryDTO getLaboratoryById(Long id) {
        logger.info("Fetching laboratory with ID: {}", id);

        Laboratory lab = laboratoryRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Laboratory with ID {} not found", id);
                    return new LaboratoryNotFoundException(id);
                });
        return LaboratoryMapper.convertToDTO(lab);
    }

    public List<EquipmentDTO> getEquipmentByLaboratoryId(Long labId) {
        Laboratory laboratory = laboratoryRepository.findById(labId)
                .orElseThrow(() -> new LaboratoryNotFoundException(labId));

        return laboratory.getEquipments().stream()
                .map(EquipmentMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public LaboratoryDTO createLaboratory(LaboratoryDTO laboratoryDTO) {
        logger.info("Creating new laboratory: {}", laboratoryDTO.getLabName());
        Laboratory laboratory = LaboratoryMapper.convertToEntity(laboratoryDTO);

        Laboratory savedLaboratory = laboratoryRepository.save(laboratory);
        logger.info("Laboratory created with ID: {}", savedLaboratory.getId());
        return LaboratoryMapper.convertToDTO(savedLaboratory);
    }

    public LaboratoryDTO updateLaboratory(Long id, LaboratoryDTO laboratoryDTO) {
        logger.info("Updating laboratory with ID: {}", id);

        Laboratory existing = laboratoryRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Cannot update. Laboratory with ID {} not found", id);
                    return new LaboratoryNotFoundException(id);
                });

        existing.setLabName(laboratoryDTO.getLabName());
        existing.setDescription(laboratoryDTO.getDescription());
        existing.setLocation(laboratoryDTO.getLocation());

        Laboratory updated = laboratoryRepository.save(existing);
        logger.info("Laboratory with ID {} updated successfully", id);
        return LaboratoryMapper.convertToDTO(updated);
    }

    public void deleteLaboratory(Long id) {
        logger.info("Deleting laboratory with ID: {}", id);

        Laboratory lab = laboratoryRepository.findById(id)
                .orElseThrow(() -> {
                    logger.error("Cannot delete. Laboratory with ID {} not found", id);
                    return new LaboratoryNotFoundException(id);
                });
        laboratoryRepository.delete(lab);
        logger.info("Laboratory with ID {} deleted successfully", id);
    }

    public Page<LaboratoryDTO> searchLaboratories(String name, String location, Pageable pageable) {
        logger.info("Searching laboratories with filters - name: {}, location: {}", name, location);

        if (name != null && name.trim().isEmpty()) {
            logger.warn("Invalid lab name received: blank string");
            throw new InvalidInputException("Invalid lab name: must not be empty.");
        }

        if (location != null && !location.matches("^[a-zA-Z0-9\\s]+$")) {
            logger.warn("Invalid location format received: {}", location);
            throw new InvalidInputException("Invalid location format: " + location);
        }

        Page<LaboratoryDTO> results = laboratoryRepository.searchLaboratoryByNameAndLocation(name, location, pageable);
        logger.info("Search completed. Found {} laboratories", results.getTotalElements());
        return results;
    }
}
