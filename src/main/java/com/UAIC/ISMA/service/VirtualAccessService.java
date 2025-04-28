package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dao.AccessRequest;
import com.UAIC.ISMA.dao.VirtualAccess;
import com.UAIC.ISMA.dto.VirtualAccessDTO;
import com.UAIC.ISMA.exception.EntityNotFoundException;
import com.UAIC.ISMA.exception.InvalidInputException;
import com.UAIC.ISMA.repository.AccessRequestRepository;
import com.UAIC.ISMA.repository.VirtualAccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
        VirtualAccess virtualAccess = convertToEntity(dto);
        VirtualAccess saved = virtualAccessRepository.save(virtualAccess);
        return convertToDTO(saved);
    }

    public void deleteById(Long id) {
        virtualAccessRepository.deleteById(id);
    }

    public VirtualAccessDTO update(Long id, VirtualAccessDTO dto) {
        return virtualAccessRepository.findById(id)
                .map(existing -> {
                    existing.setUsername(dto.getUsername());
                    existing.setPassword(dto.getPassword());
                    existing.setIssuedDate(dto.getIssuedDate());
                    AccessRequest accessRequest = accessRequestRepository.findById(dto.getAccessRequestId())
                            .orElseThrow(() -> new EntityNotFoundException("AccessRequest not found with id " + dto.getAccessRequestId()));
                    existing.setAccessRequest(accessRequest);
                    VirtualAccess updated = virtualAccessRepository.save(existing);
                    return convertToDTO(updated);
                })
                .orElseThrow(() -> new EntityNotFoundException("VirtualAccess not found with id " + id));
    }

    private VirtualAccessDTO convertToDTO(VirtualAccess virtualAccess) {
        return new VirtualAccessDTO(
                virtualAccess.getAccessRequest().getId(),
                virtualAccess.getUsername(),
                virtualAccess.getPassword(),
                virtualAccess.getIssuedDate()
        );
    }

    private VirtualAccess convertToEntity(VirtualAccessDTO dto) {
        AccessRequest accessRequest = accessRequestRepository.findById(dto.getAccessRequestId())
                .orElseThrow(() -> new EntityNotFoundException("AccessRequest not found with id " + dto.getAccessRequestId()));
        return new VirtualAccess(
                dto.getUsername(),
                dto.getPassword(),
                accessRequest
        );
    }
}
