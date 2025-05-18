package com.UAIC.ISMA.service;

import com.UAIC.ISMA.entity.VirtualAccess;
import com.UAIC.ISMA.dto.VirtualAccessDTO;
import com.UAIC.ISMA.exception.InvalidInputException;
import com.UAIC.ISMA.exception.VirtualAccessNotFoundException;
import com.UAIC.ISMA.mapper.VirtualAccessMapper;
import com.UAIC.ISMA.repository.VirtualAccessRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
public class VirtualAccessService {

    private final VirtualAccessRepository virtualAccessRepository;

    @Autowired
    public VirtualAccessService(VirtualAccessRepository virtualAccessRepository) {
        this.virtualAccessRepository = virtualAccessRepository;
    }

    public List<VirtualAccessDTO> findAll() {
        return virtualAccessRepository.findAll()
                .stream()
                .map(VirtualAccessMapper::toDTO)
                .collect(Collectors.toList());
    }

    public VirtualAccessDTO findById(Long id) {
        VirtualAccess virtualAccess = virtualAccessRepository.findById(id)
                .orElseThrow(() -> new VirtualAccessNotFoundException(id));
        return VirtualAccessMapper.toDTO(virtualAccess);
    }

    public VirtualAccessDTO create(VirtualAccessDTO dto) {
        if (dto.getIssuedDate() == null) {
            dto.setIssuedDate(LocalDateTime.now());
        }
        if (dto.getUsername() == null || dto.getUsername().isEmpty()) {
            throw new InvalidInputException("Username cannot be empty");
        }
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            throw new InvalidInputException("Password cannot be empty");
        }

        VirtualAccess entity = VirtualAccessMapper.toEntity(dto);
        VirtualAccess savedEntity = virtualAccessRepository.save(entity);
        return VirtualAccessMapper.toDTO(savedEntity);
    }

    public void deleteById(Long id) {
        VirtualAccess virtualAccess = virtualAccessRepository.findById(id)
                .orElseThrow(() -> new VirtualAccessNotFoundException(id));
        virtualAccessRepository.delete(virtualAccess);
    }

    public VirtualAccessDTO update(Long id, VirtualAccessDTO dto) {
        if (dto.getIssuedDate() == null) {
            dto.setIssuedDate(LocalDateTime.now());
        }
        if (dto.getUsername() == null || dto.getUsername().isEmpty()) {
            throw new InvalidInputException("Username cannot be empty");
        }
        if (dto.getPassword() == null || dto.getPassword().isEmpty()) {
            throw new InvalidInputException("Password cannot be empty");
        }

        VirtualAccess existing = virtualAccessRepository.findById(id)
                .orElseThrow(() -> new VirtualAccessNotFoundException(id));

        existing.setUsername(dto.getUsername());
        existing.setPassword(dto.getPassword());

        VirtualAccess updatedVirtualAccess = virtualAccessRepository.save(existing);
        return VirtualAccessMapper.toDTO(updatedVirtualAccess);
    }

}
