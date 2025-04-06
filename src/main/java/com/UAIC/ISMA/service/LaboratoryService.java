package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dao.Laboratory;
import com.UAIC.ISMA.dto.LaboratoryDTO;
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
        Laboratory laboratory = new Laboratory();
        return convertToDTO(laboratoryRepository.save(laboratory));
    }

    public LaboratoryDTO updateLaboratory(Long id, LaboratoryDTO laboratoryDTO) {
        Laboratory laboratory = convertToEntity(laboratoryDTO);
        laboratory.setId(id);
        return convertToDTO(laboratoryRepository.save(laboratory));
    }

    public void deleteLaboratory(Long id) {
        laboratoryRepository.deleteById(id);
    }

    public LaboratoryDTO partialUpdateLaboratory(Long id, Map<String, Object> updates) {
        Laboratory laboratory = laboratoryRepository.findById(id).orElseThrow(()
                -> new RuntimeException("Laboratory with id " + id + " not found"));

        updates.forEach((key, value) -> {
            switch (key) {
                case "name" -> laboratory.setName((String) value);
                case "description" -> laboratory.setDescription((String) value);
                default -> throw new RuntimeException("Invalid key " + key);
            }
        });

        Laboratory updated = laboratoryRepository.save(laboratory);
        return convertToDTO(updated);
    }

    private LaboratoryDTO convertToDTO(Laboratory lab) {
        LaboratoryDTO dto = new LaboratoryDTO();
        dto.setId(lab.getId());
        dto.setName(lab.getName());
        dto.setDescription(lab.getDescription());
        return dto;
    }

    private Laboratory convertToEntity(LaboratoryDTO dto) {
        Laboratory lab = new Laboratory();
        lab.setName(dto.getName());
        lab.setDescription(dto.getDescription());
        return lab;
    }

}
