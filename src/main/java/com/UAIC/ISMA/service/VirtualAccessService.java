package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dto.VirtualAccessDTO;
import com.UAIC.ISMA.entity.AccessRequest;
import com.UAIC.ISMA.entity.VirtualAccess;
import com.UAIC.ISMA.exception.InvalidInputException;
import com.UAIC.ISMA.exception.VirtualAccessAlreadyExistsException;
import com.UAIC.ISMA.exception.VirtualAccessNotFoundException;
import com.UAIC.ISMA.mapper.VirtualAccessMapper;
import com.UAIC.ISMA.repository.AccessRequestRepository;
import com.UAIC.ISMA.repository.VirtualAccessRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
public class VirtualAccessService {

    private final VirtualAccessRepository virtualAccessRepository;
    private final AccessRequestRepository accessRequestRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public List<VirtualAccessDTO> findAll() {
        return virtualAccessRepository.findAll().stream()
                .map(VirtualAccessMapper::toDTO)
                .collect(toList());
    }

    public VirtualAccessDTO findById(Long id) {
        return virtualAccessRepository.findById(id)
                .map(VirtualAccessMapper::toDTO)
                .orElseThrow(() -> new VirtualAccessNotFoundException(id));
    }

    public VirtualAccessDTO create(VirtualAccessDTO dto) {
        validate(dto);

        VirtualAccess entity = VirtualAccessMapper.toEntity(dto);
        if (dto.getAccessRequestId() != null) {
            entity.setAccessRequest(getAccessRequestOrThrow(dto.getAccessRequestId()));
        }

        try {
            return VirtualAccessMapper.toDTO(virtualAccessRepository.save(entity));
        } catch (DataIntegrityViolationException e) {
            throw new VirtualAccessAlreadyExistsException(dto.getAccessRequestId());
        }
    }

    public VirtualAccessDTO update(Long id, VirtualAccessDTO dto) {
        validate(dto);

        VirtualAccess existing = virtualAccessRepository.findById(id)
                .orElseThrow(() -> new VirtualAccessNotFoundException(id));

        existing.setUsername(dto.getUsername());
        existing.setPassword(dto.getPassword());

        if (dto.getAccessRequestId() != null) {
            existing.setAccessRequest(getAccessRequestOrThrow(dto.getAccessRequestId()));
        }

        try {
            return VirtualAccessMapper.toDTO(virtualAccessRepository.save(existing));
        } catch (DataIntegrityViolationException e) {
            throw new VirtualAccessAlreadyExistsException(dto.getAccessRequestId());
        }
    }


    @Transactional
    public void deleteById(Long id) {
        VirtualAccess va = virtualAccessRepository.findById(id)
                .orElseThrow(() -> new VirtualAccessNotFoundException(id));

        AccessRequest request = va.getAccessRequest();
        if (request != null) {
            request.setVirtualAccess(null);
        }

        virtualAccessRepository.delete(va);
    }


    @Transactional
    public VirtualAccessDTO createVirtualAccessForRequest(AccessRequest request) {
        if (request.getVirtualAccess() != null) {
            throw new IllegalStateException("VirtualAccess already exists for this request.");
        }

        String username = generateUsername(request);
        String rawPassword = generateRandomPassword();
        String encryptedPassword = passwordEncoder.encode(rawPassword);

        VirtualAccess va = new VirtualAccess(username, encryptedPassword, request);
        request.setVirtualAccess(va);

        virtualAccessRepository.save(va);
        emailService.sendVirtualAccessCredentials(request.getUser().getEmail(), username, rawPassword);

        return VirtualAccessMapper.toDTO(va);
    }


    private void validate(VirtualAccessDTO dto) {
        if (dto.getUsername() == null || dto.getUsername().isBlank()) {
            throw new InvalidInputException("Username cannot be empty.");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new InvalidInputException("Password cannot be empty.");
        }
    }

    private AccessRequest getAccessRequestOrThrow(Long id) {
        return accessRequestRepository.findById(id)
                .orElseThrow(() -> new InvalidInputException("AccessRequest not found with ID: " + id));
    }

    private String generateUsername(AccessRequest request) {
        String[] emailParts = request.getUser().getEmail().split("@")[0].split("\\.");
        String firstInitial = emailParts[0].substring(0, 1).toLowerCase();
        String lastName = emailParts.length > 1 ? emailParts[1].toLowerCase() : "";
        String equipment = request.getEquipment().getName().toLowerCase().replaceAll("\\s+", "-");
        return String.format("%s%s.%s.%d", firstInitial, lastName, equipment, request.getId());
    }

    private String generateRandomPassword() {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*-_";
        SecureRandom random = new SecureRandom();
        return random.ints(14, 0, chars.length())
                .mapToObj(i -> String.valueOf(chars.charAt(i)))
                .reduce("", String::concat);
    }
}
