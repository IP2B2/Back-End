package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dao.Laboratory;
import com.UAIC.ISMA.dto.LaboratoryDTO;
import com.UAIC.ISMA.exception.LaboratoryNotFoundException;
import com.UAIC.ISMA.mapper.LaboratoryMapper;
import com.UAIC.ISMA.repository.LaboratoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LaboratoryService {

    public final LaboratoryRepository laboratoryRepository;

    public LaboratoryService(LaboratoryRepository laboratoryRepository) {
        this.laboratoryRepository = laboratoryRepository;
    }

    public List<LaboratoryDTO> getAlLaboratories() {
        return laboratoryRepository.findAll().stream()
                .map(LaboratoryMapper::convertToDTO).collect(Collectors.toList());
    }

    public LaboratoryDTO getLaboratoryById(Long id) {
        Laboratory lab = laboratoryRepository.findById(id)
            .orElseThrow(() -> new LaboratoryNotFoundException(id));
        return LaboratoryMapper.convertToDTO(lab);
    }

    public LaboratoryDTO createLaboratory(LaboratoryDTO laboratoryDTO) {
        Laboratory laboratory = LaboratoryMapper.convertToEntity(laboratoryDTO);
        Laboratory savedLaboratory = laboratoryRepository.save(laboratory);
        return LaboratoryMapper.convertToDTO(savedLaboratory);
    }

    public LaboratoryDTO updateLaboratory(Long id, LaboratoryDTO laboratoryDTO) {
        Laboratory existing = laboratoryRepository.findById(id)
                .orElseThrow(() -> new LaboratoryNotFoundException(id));

       existing.setLabName(laboratoryDTO.getLabName());
       existing.setDescription(laboratoryDTO.getDescription());
       existing.setLocation(laboratoryDTO.getLocation());

        return LaboratoryMapper.convertToDTO(laboratoryRepository.save(existing));
    }

    public void deleteLaboratory(Long id) {
        Laboratory lab = laboratoryRepository.findById(id)
                .orElseThrow(() -> new LaboratoryNotFoundException(id));
        laboratoryRepository.delete(lab);
    }




}
