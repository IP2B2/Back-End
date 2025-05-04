package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dao.AccessRequest;
import com.UAIC.ISMA.dao.VirtualAccess;
import com.UAIC.ISMA.dto.VirtualAccessDTO;
import com.UAIC.ISMA.exception.EntityNotFoundException;
import com.UAIC.ISMA.exception.InvalidInputException;
import com.UAIC.ISMA.repository.AccessRequestRepository;
import com.UAIC.ISMA.repository.VirtualAccessRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class VirtualAccessService {

    private final VirtualAccessRepository virtualAccessRepository;
    private final AccessRequestRepository accessRequestRepository;

    @Autowired
    public VirtualAccessService(VirtualAccessRepository virtualAccessRepository, AccessRequestRepository accessRequestRepository) {
        this.virtualAccessRepository = virtualAccessRepository;
        this.accessRequestRepository = accessRequestRepository;
    }

    public List<VirtualAccessDTO> findAll() {
        return virtualAccessRepository.findAll()
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<VirtualAccessDTO> findById(Long id) {
        return virtualAccessRepository.findById(id)
                .map(this::convertToDTO);
    }

    public VirtualAccessDTO save(VirtualAccessDTO dto) {
        if (dto.getUsername() == null || dto.getUsername().isEmpty()) {
            throw new InvalidInputException("Username cannot be empty");
        }
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            throw new InvalidInputException("Password cannot be empty");
        }
        if (virtualAccessRepository.existsByAccessRequest_Id(dto.getAccessRequestId())) {
            throw new InvalidInputException("This access request already has virtual access credentials assigned.");
        }

        AccessRequest accessRequest = accessRequestRepository.findById(dto.getAccessRequestId())
                .orElseThrow(() -> new EntityNotFoundException("AccessRequest not found"));

        VirtualAccess entity = new VirtualAccess(dto.getUsername(), dto.getPassword(), accessRequest);
        entity.setIssuedDate(dto.getIssuedDate() != null ? dto.getIssuedDate() : LocalDateTime.now());

        VirtualAccess saved = virtualAccessRepository.save(entity);
        return convertToDTO(saved);
    }


    public void deleteById(Long id) {
        VirtualAccess virtualAccess = virtualAccessRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("VirtualAccess not found with id " + id));

        AccessRequest accessRequest = virtualAccess.getAccessRequest();
        if (accessRequest != null) {
            accessRequest.setVirtualAccess(null);
        }
        virtualAccessRepository.delete(virtualAccess);
    }


    public VirtualAccessDTO update(Long id, VirtualAccessDTO dto) {
        if (dto.getIssuedDate() == null) {
            dto.setIssuedDate(LocalDateTime.now());
        }

        return virtualAccessRepository.findById(id)
                .map(existing -> {
                    existing.setUsername(dto.getUsername());
                    existing.setPassword(dto.getPassword());
                    existing.setIssuedDate(dto.getIssuedDate());

                    if (dto.getAccessRequestId() != null) {
                        Optional<VirtualAccess> conflict = virtualAccessRepository
                                .findByAccessRequest_Id(dto.getAccessRequestId());

                        if (conflict.isPresent() && !conflict.get().getId().equals(id)) {
                            throw new InvalidInputException("Another virtual access already uses this access request.");
                        }

                        AccessRequest accessRequest = accessRequestRepository.findById(dto.getAccessRequestId())
                                .orElseThrow(() -> new EntityNotFoundException("AccessRequest not found with id " + dto.getAccessRequestId()));
                        existing.setAccessRequest(accessRequest);
                    }

                    VirtualAccess updated = virtualAccessRepository.save(existing);
                    return convertToDTO(updated);
                })
                .orElseThrow(() -> new EntityNotFoundException("VirtualAccess not found with id " + id));
    }


    private VirtualAccessDTO convertToDTO(VirtualAccess virtualAccess) {
        VirtualAccessDTO dto = new VirtualAccessDTO();
        dto.setId(virtualAccess.getId());
        dto.setUsername(virtualAccess.getUsername());
        dto.setPassword(virtualAccess.getPassword());
        dto.setIssuedDate(virtualAccess.getIssuedDate());
        dto.setAccessRequestId(virtualAccess.getAccessRequest().getId());
        return dto;
    }

}
