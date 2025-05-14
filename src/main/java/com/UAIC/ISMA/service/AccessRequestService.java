package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dao.AccessRequest;
import com.UAIC.ISMA.dao.Equipment;
import com.UAIC.ISMA.dao.User;
import com.UAIC.ISMA.dto.AccessRequestDTO;
import com.UAIC.ISMA.exception.AccessRequestNotFoundException;
import com.UAIC.ISMA.exception.UserNotFoundException;
import com.UAIC.ISMA.exception.EquipmentNotFoundException;
import com.UAIC.ISMA.mapper.AccessRequestMapper;
import com.UAIC.ISMA.repository.AccessRequestRepository;
import com.UAIC.ISMA.repository.EquipmentRepository;
import com.UAIC.ISMA.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import com.UAIC.ISMA.dao.enums.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.time.LocalDate;

@Service
public class AccessRequestService {

    private final AccessRequestRepository accessRequestRepository;
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;

    @Autowired
    public AccessRequestService(
            AccessRequestRepository accessRequestRepository,
            UserRepository userRepository,
            EquipmentRepository equipmentRepository
    ) {
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
                .orElseThrow(() -> new AccessRequestNotFoundException(id));
        return AccessRequestMapper.toDTO(entity);
    }

    public AccessRequestDTO create(AccessRequestDTO dto) {
        AccessRequest entity = AccessRequestMapper.toEntity(dto);

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(dto.getUserId()));
        Equipment equipment = equipmentRepository.findById(dto.getEquipmentId())
                .orElseThrow(() -> new EquipmentNotFoundException(dto.getEquipmentId()));

        entity.setUser(user);
        entity.setEquipment(equipment);

        return AccessRequestMapper.toDTO(accessRequestRepository.save(entity));
    }

    public AccessRequestDTO update(Long id, AccessRequestDTO dto) {
        AccessRequest existing = accessRequestRepository.findById(id)
                .orElseThrow(() -> new AccessRequestNotFoundException(id));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(dto.getUserId()));
        Equipment equipment = equipmentRepository.findById(dto.getEquipmentId())
                .orElseThrow(() -> new EquipmentNotFoundException(dto.getEquipmentId()));

        existing.setRequestDate(dto.getRequestDate());
        existing.setStatus(dto.getStatus());
        existing.setRequestType(dto.getRequestType());
        existing.setProposalFile(dto.getProposalFile());
        existing.setExpectedReturnDate(dto.getExpectedReturnDate());
        existing.setUser(user);
        existing.setEquipment(equipment);

        return AccessRequestMapper.toDTO(accessRequestRepository.save(existing));
    }

    public AccessRequestDTO updatePartial(Long id, Map<String, Object> updates) {
        AccessRequest existing = accessRequestRepository.findById(id)
                .orElseThrow(() -> new AccessRequestNotFoundException(id));

        updates.forEach((key, value) -> {
            switch (key) {
                case "status" -> existing.setStatus(Enum.valueOf(com.UAIC.ISMA.dao.enums.RequestStatus.class, value.toString()));
                case "requestType" -> existing.setRequestType(Enum.valueOf(com.UAIC.ISMA.dao.enums.RequestType.class, value.toString()));
                case "proposalFile" -> existing.setProposalFile(value.toString());
                case "expectedReturnDate" -> existing.setExpectedReturnDate(LocalDateTime.parse(value.toString()));
            }
        });

        return AccessRequestMapper.toDTO(accessRequestRepository.save(existing));
    }

    public void delete(Long id) {
        AccessRequest existing = accessRequestRepository.findById(id)
                .orElseThrow(() -> new AccessRequestNotFoundException(id));
        accessRequestRepository.delete(existing);
    }

    public List<AccessRequestDTO> findByUserWithFilters(Long userId, RequestStatus status, LocalDate date, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<AccessRequest> pageResult = accessRequestRepository.findByUserWithFilters(userId, status, date, pageable);

        return pageResult.getContent().stream()
                .map(AccessRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

}
