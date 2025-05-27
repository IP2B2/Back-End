package com.UAIC.ISMA.service;

import com.UAIC.ISMA.entity.AccessRequest;
import com.UAIC.ISMA.entity.Equipment;
import com.UAIC.ISMA.entity.User;
import com.UAIC.ISMA.dto.AccessRequestDTO;
import com.UAIC.ISMA.entity.enums.RequestStatus;
import com.UAIC.ISMA.entity.enums.RequestType;
import com.UAIC.ISMA.exception.*;
import com.UAIC.ISMA.mapper.AccessRequestMapper;
import com.UAIC.ISMA.repository.AccessRequestRepository;
import com.UAIC.ISMA.repository.EquipmentRepository;
import com.UAIC.ISMA.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class AccessRequestService {

    private static final Logger logger = LoggerFactory.getLogger(AccessRequestService.class);
    public static final int DEFAULT_PAGE_SIZE = 10;

    private final AccessRequestRepository accessRequestRepository;
    private final UserRepository userRepository;
    private final EquipmentRepository equipmentRepository;
    private final VirtualAccessService virtualAccessService;

    @Autowired
    public AccessRequestService(
            AccessRequestRepository accessRequestRepository,
            UserRepository userRepository,
            EquipmentRepository equipmentRepository,
            VirtualAccessService virtualAccessService
    ) {
        this.accessRequestRepository = accessRequestRepository;
        this.userRepository = userRepository;
        this.equipmentRepository = equipmentRepository;
        this.virtualAccessService = virtualAccessService;
    }

    public List<AccessRequestDTO> findAll() {
        logger.info("Fetching all AccessRequests...");
        return accessRequestRepository.findAll()
                .stream()
                .map(AccessRequestMapper::toDTO)
                .collect(Collectors.toList());
    }

    public AccessRequestDTO findById(Long id) {
        logger.info("Fetching AccessRequest with ID={}", id);
        AccessRequest entity = accessRequestRepository.findById(id)
                .orElseThrow(() -> new AccessRequestNotFoundException(id));
        return AccessRequestMapper.toDTO(entity);
    }

    public AccessRequestDTO create(AccessRequestDTO dto) {
        if (dto.getEquipmentId() == null) {
            throw new MissingFieldException("Equipment ID");
        }

        String username = ((UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUsername();

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));

        logger.info("Creating AccessRequest for userId={} and equipmentId={}", user.getId(), dto.getEquipmentId());
        AccessRequest entity = AccessRequestMapper.toEntity(dto);

        Equipment equipment = equipmentRepository.findById(dto.getEquipmentId())
                .orElseThrow(() -> new EquipmentNotFoundException(dto.getEquipmentId()));

        updateEntityFromDto(entity, dto, user, equipment);
        AccessRequest saved = accessRequestRepository.save(entity);
        logger.info("AccessRequest created with ID={}", saved.getId());
        return AccessRequestMapper.toDTO(saved);
    }

    public AccessRequestDTO update(Long id, AccessRequestDTO dto) {
        logger.info("Updating AccessRequest with ID={}", id);
        AccessRequest existing = accessRequestRepository.findById(id)
                .orElseThrow(() -> new AccessRequestNotFoundException(id));

        if (dto.getUserId() == null) {
            throw new MissingFieldException("User ID");
        }

        if (dto.getEquipmentId() == null) {
            throw new MissingFieldException("Equipment ID");
        }

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new UserNotFoundException(dto.getUserId()));

        Equipment equipment = equipmentRepository.findById(dto.getEquipmentId())
                .orElseThrow(() -> new EquipmentNotFoundException(dto.getEquipmentId()));

        updateEntityFromDto(existing, dto, user, equipment);
        AccessRequest updated = accessRequestRepository.save(existing);
        logger.info("AccessRequest with ID={} updated successfully", id);
        return AccessRequestMapper.toDTO(updated);
    }

    public AccessRequestDTO updatePartial(Long id, Map<String, Object> updates) {
        logger.info("Partially updating AccessRequest with ID={}", id);
        AccessRequest existing = accessRequestRepository.findById(id)
                .orElseThrow(() -> new AccessRequestNotFoundException(id));

        updates.forEach((key, value) -> {
            try {
                switch (key) {
                    case "status" -> existing.setStatus(RequestStatus.valueOf(value.toString()));
                    case "requestType" -> existing.setRequestType(RequestType.valueOf(value.toString()));
                    case "proposalFile" -> existing.setProposalFile(value.toString());
                    case "expectedReturnDate" -> existing.setExpectedReturnDate(LocalDateTime.parse(value.toString()));
                    default -> throw new InvalidInputException("Invalid field: '" + key + "' is not recognized.");
                }
            } catch (IllegalArgumentException ex) {
                logger.error("Invalid value '{}' for field '{}'", value, key);
                throw new InvalidInputException("Invalid value for field '" + key + "': " + value);
            }
        });

        return AccessRequestMapper.toDTO(accessRequestRepository.save(existing));
    }

    @Transactional
    public AccessRequestDTO approveAccessRequest(Long requestId) {
        AccessRequest request = accessRequestRepository.findById(requestId)
                .orElseThrow(() -> new AccessRequestNotFoundException(requestId));

        if (request.getStatus() == RequestStatus.APPROVED) {
            throw new ConflictException("AccessRequest with ID " + requestId + " is already approved.");
        }

        request.setStatus(RequestStatus.APPROVED);

        if (request.getRequestType() == RequestType.VIRTUAL) {
            virtualAccessService.createVirtualAccessForRequest(request);
        }

        return AccessRequestMapper.toDTO(accessRequestRepository.save(request));
    }

    public void delete(Long id) {
        logger.info("Deleting AccessRequest with ID={}", id);
        AccessRequest existing = accessRequestRepository.findById(id)
                .orElseThrow(() -> new AccessRequestNotFoundException(id));
        accessRequestRepository.delete(existing);
        logger.info("AccessRequest with ID={} deleted successfully", id);
    }

    public Page<AccessRequestDTO> filterRequests(RequestStatus status, String equipmentType, Long userId, Pageable pageable) {
        logger.info("Filtering AccessRequests by status={}, equipmentType={}, userId={}", status, equipmentType, userId);

        if (equipmentType != null && !equipmentType.isBlank()) {
            equipmentType = "%" + equipmentType + "%";
        }

        return accessRequestRepository.filterAccessRequests(status, equipmentType, userId, pageable);
    }

    public List<AccessRequestDTO> findByUserWithFilters(Long userId, RequestStatus status, LocalDate date, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        if (status == null && date == null) {
            return accessRequestRepository.findByUserId(userId, pageable).getContent();
        }

        if (status != null && date == null) {
            return accessRequestRepository.findByUserIdAndStatus(userId, status, pageable).getContent();
        }

        if (status == null && date != null) {
            LocalDateTime start = date.atStartOfDay();
            LocalDateTime end = date.plusDays(1).atStartOfDay();
            return accessRequestRepository.findByUserIdAndDateBetween(userId, start, end, pageable).getContent();
        }

        LocalDateTime start = date.atStartOfDay();
        LocalDateTime end = date.plusDays(1).atStartOfDay();
        return accessRequestRepository.findByUserIdAndStatusAndDateBetween(userId, status, start, end, pageable).getContent();
    }

    private void updateEntityFromDto(AccessRequest entity, AccessRequestDTO dto, User user, Equipment equipment) {
        entity.setRequestDate(dto.getRequestDate());
        entity.setStatus(dto.getStatus());
        entity.setRequestType(dto.getRequestType());
        entity.setProposalFile(dto.getProposalFile());
        entity.setExpectedReturnDate(dto.getExpectedReturnDate());
        entity.setUser(user);
        entity.setEquipment(equipment);
    }
}