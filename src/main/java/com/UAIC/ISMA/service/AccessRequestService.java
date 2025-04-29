package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dao.AccessRequest;
import com.UAIC.ISMA.dao.Equipment;
import com.UAIC.ISMA.dao.User;
import com.UAIC.ISMA.dto.AccessRequestDTO;
import com.UAIC.ISMA.exception.EntityNotFoundException;
import com.UAIC.ISMA.mapper.AccessRequestMapper;
import com.UAIC.ISMA.repository.AccessRequestRepository;
import com.UAIC.ISMA.repository.EquipmentRepository;
import com.UAIC.ISMA.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AccessRequestService {

    private final AccessRequestRepository accessRequestRepository;
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;

    public AccessRequestService(AccessRequestRepository accessRequestRepository,
                                UserRepository userRepository,
                                EquipmentRepository equipmentRepository) {
        this.accessRequestRepository = accessRequestRepository;
        this.userRepository = userRepository;
        this.equipmentRepository = equipmentRepository;
    }

    public List<AccessRequestDTO> findAll() {
        return accessRequestRepository.findAll()
                .stream()
                .map(AccessRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    public AccessRequestDTO findById(Long id) {
        AccessRequest entity = accessRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AccessRequest not found"));
        return AccessRequestMapper.toDTO(entity);
    }

    public AccessRequestDTO create(AccessRequestDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Equipment equipment = equipmentRepository.findById(dto.getEquipmentId())
                .orElseThrow(() -> new EntityNotFoundException("Equipment not found"));

        AccessRequest entity = AccessRequestMapper.toEntity(dto);
        entity.setUser(user);
        entity.setEquipment(equipment);

        return AccessRequestMapper.toDTO(accessRequestRepository.save(entity));
    }

    public AccessRequestDTO update(Long id, AccessRequestDTO dto) {
        AccessRequest existing = accessRequestRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AccessRequest not found"));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        Equipment equipment = equipmentRepository.findById(dto.getEquipmentId())
                .orElseThrow(() -> new EntityNotFoundException("Equipment not found"));

        existing.setRequestDate(dto.getRequestDate());
        existing.setStatus(dto.getStatus());
        existing.setRequestType(dto.getRequestType());
        existing.setProposalFile(dto.getProposalFile());
        existing.setExpectedReturnDate(dto.getExpectedReturnDate());
        existing.setUser(user);
        existing.setEquipment(equipment);

        return AccessRequestMapper.toDTO(accessRequestRepository.save(existing));
    }

    public void delete(Long id) {
        accessRequestRepository.deleteById(id);
    }
}
