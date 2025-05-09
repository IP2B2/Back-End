package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dao.Equipment;
import com.UAIC.ISMA.dto.EquipmentDTO;
import com.UAIC.ISMA.exception.EquipmentNotFoundException;
import com.UAIC.ISMA.mapper.EquipmentMapper;
import com.UAIC.ISMA.repository.EquipmentRepository;
import com.UAIC.ISMA.repository.LaboratoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EquipmentService {

    private final EquipmentRepository equipmentRepository;
    private final LaboratoryRepository laboratoryRepository;

    public EquipmentService(EquipmentRepository equipmentRepository,
                            LaboratoryRepository laboratoryRepository) {
        this.equipmentRepository = equipmentRepository;
        this.laboratoryRepository = laboratoryRepository;
    }

    public EquipmentDTO createEquipment(EquipmentDTO dto) {
        Equipment equipment = EquipmentMapper.convertToEntity(dto, laboratoryRepository);
        Equipment saved = equipmentRepository.save(equipment);
        return EquipmentMapper.convertToDTO(saved);
    }

    public EquipmentDTO getEquipmentById(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new EquipmentNotFoundException(id));
        return EquipmentMapper.convertToDTO(equipment);
    }

    public List<EquipmentDTO> getAllEquipments(Long laboratoryId) {
        return equipmentRepository.findAll().stream()
                .map(EquipmentMapper::convertToDTO)
                .collect(Collectors.toList());
    }

    public EquipmentDTO updateEquipment(EquipmentDTO dto, Long id) {
        Equipment existing = equipmentRepository.findById(id)
                .orElseThrow(() -> new EquipmentNotFoundException(id));

        Equipment updated = EquipmentMapper.convertToEntity(dto, laboratoryRepository);
        updated.setId(id);
        updated.setAccessRequests(existing.getAccessRequests());

        Equipment saved = equipmentRepository.save(updated);
        return EquipmentMapper.convertToDTO(saved);
    }

    public void deleteEquipment(Long id) {
        Equipment equipment = equipmentRepository.findById(id)
                .orElseThrow(() -> new EquipmentNotFoundException(id));
        equipmentRepository.delete(equipment);
    }
}
