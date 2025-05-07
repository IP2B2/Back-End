package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dao.Laboratory;
import com.UAIC.ISMA.dto.LaboratoryDTO;
import com.UAIC.ISMA.exception.EntityNotFoundException;
import com.UAIC.ISMA.exception.InvalidInputException;
import com.UAIC.ISMA.repository.LaboratoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LaboratoryService {

    public final LaboratoryRepository laboratoryRepository;

    public LaboratoryService(LaboratoryRepository laboratoryRepository) {
        this.laboratoryRepository = laboratoryRepository;
    }

    public List<LaboratoryDTO> getAlLaboratories() {
        return laboratoryRepository.findAll().stream()
                .map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<LaboratoryDTO> getLaboratoryById(Long id) {
        return laboratoryRepository.findById(id).map(this::convertToDTO);
    }

    public LaboratoryDTO createLaboratory(LaboratoryDTO laboratoryDTO) {
        Laboratory laboratory = convertToEntity(laboratoryDTO);
        Laboratory savedLaboratory = laboratoryRepository.save(laboratory);
        return convertToDTO(savedLaboratory);
    }

    public LaboratoryDTO updateLaboratory(Long id, LaboratoryDTO laboratoryDTO) {
        Laboratory existing = laboratoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Laboratory not found with id: " + id));

        Laboratory laboratory = convertToEntity(laboratoryDTO);
        laboratory.setId(id);

        laboratory.setEquipments(existing.getEquipments());
        laboratory.setLabDocuments(existing.getLabDocuments());

        Laboratory saved = laboratoryRepository.save(laboratory);
        return convertToDTO(saved);
    }

    public void deleteLaboratory(Long id) {
        laboratoryRepository.deleteById(id);
    }

    public LaboratoryDTO partialUpdateLaboratory(Long id, Map<String, Object> updates) {
        Laboratory laboratory = laboratoryRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("Laboratory with id " + id + " not found"));

        updates.forEach((key, value) -> {
            switch (key) {
                case "labName" -> laboratory.setLabName((String) value);
                case "description" -> laboratory.setDescription((String) value);
                case "location" -> laboratory.setLocation((String) value);
                default -> throw new InvalidInputException("Invalid key " + key);
            }
        });

        Laboratory updated = laboratoryRepository.save(laboratory);
        return convertToDTO(updated);
    }

    private LaboratoryDTO convertToDTO(Laboratory lab) {
        LaboratoryDTO dto = new LaboratoryDTO();
        dto.setId(lab.getId());
        dto.setLabName(lab.getLabName());
        dto.setDescription(lab.getDescription());
        dto.setLocation(lab.getLocation());

        if(lab.getEquipments() != null) {
            dto.setEquipmentIds(lab.getEquipments().stream()
                    .map(e -> e.getId()).collect(Collectors.toList()));
        }

        if(lab.getLabDocuments() != null) {
            dto.setLabDocumentIds(lab.getLabDocuments().stream()
                    .map(e -> e.getId()).collect(Collectors.toList()));
        }

        return dto;
    }

    private Laboratory convertToEntity(LaboratoryDTO dto) {
        Laboratory lab = new Laboratory();
        lab.setLabName(dto.getLabName());
        lab.setDescription(dto.getDescription());
        lab.setLocation(dto.getLocation());

        return lab;
    }

}
