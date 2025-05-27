package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dto.VirtualAccessDTO;
import com.UAIC.ISMA.entity.AccessRequest;
import com.UAIC.ISMA.entity.VirtualAccess;
import com.UAIC.ISMA.exception.InvalidInputException;
import com.UAIC.ISMA.exception.MissingAccessRequestIdException;
import com.UAIC.ISMA.exception.VirtualAccessAlreadyExistsException;
import com.UAIC.ISMA.exception.VirtualAccessNotFoundException;
import com.UAIC.ISMA.mapper.VirtualAccessMapper;
import com.UAIC.ISMA.repository.AccessRequestRepository;
import com.UAIC.ISMA.repository.VirtualAccessRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class VirtualAccessService {

    private final VirtualAccessRepository virtualAccessRepository;
    private final AccessRequestRepository accessRequestRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public List<VirtualAccessDTO> findAll() {
        log.info("Fetching all virtual accesses from database.");
        return virtualAccessRepository.findAll().stream()
                .map(VirtualAccessMapper::toDTO)
                .collect(toList());
    }

    public VirtualAccessDTO findById(Long id) {
        log.info("Fetching virtual access with ID {}", id);
        return virtualAccessRepository.findById(id)
                .map(VirtualAccessMapper::toDTO)
                .orElseThrow(() -> {
                    log.warn("Virtual access not found with ID {}", id);
                    return new VirtualAccessNotFoundException(id);
                });
    }

    public VirtualAccessDTO create(VirtualAccessDTO dto) {
        log.info("Creating virtual access for request ID {}", dto.getAccessRequestId());
        validate(dto);
        if (dto.getIssuedDate() == null) {
            dto.setIssuedDate(LocalDateTime.now());
        }
        VirtualAccess entity = VirtualAccessMapper.toEntity(dto);
        if (dto.getAccessRequestId() != null) {
            entity.setAccessRequest(getAccessRequestOrThrow(dto.getAccessRequestId()));
        }

        try {
            VirtualAccess saved = virtualAccessRepository.save(entity);
            log.info("Virtual access created with ID {}", saved.getId());
            return VirtualAccessMapper.toDTO(saved);
        } catch (DataIntegrityViolationException e) {
            log.warn("Attempt to create duplicate virtual access for request ID {}", dto.getAccessRequestId());
            throw new VirtualAccessAlreadyExistsException(dto.getAccessRequestId());
        }
    }

    public VirtualAccessDTO update(Long id, VirtualAccessDTO dto) {
        log.info("Updating virtual access with ID {}", id);
        validate(dto);

        if (dto.getIssuedDate() == null) {
            dto.setIssuedDate(LocalDateTime.now());
        }

        VirtualAccess existing = virtualAccessRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Virtual access not found for update with ID {}", id);
                    return new VirtualAccessNotFoundException(id);
                });

        existing.setUsername(dto.getUsername());
        existing.setPassword(dto.getPassword());
        existing.setIssuedDate(dto.getIssuedDate());

        if (dto.getAccessRequestId() != null) {
            existing.setAccessRequest(getAccessRequestOrThrow(dto.getAccessRequestId()));
        }

        try {
            VirtualAccess updated = virtualAccessRepository.save(existing);
            log.info("Virtual access updated successfully with ID {}", updated.getId());
            return VirtualAccessMapper.toDTO(updated);
        } catch (DataIntegrityViolationException e) {
            log.warn("Duplicate request ID {} detected during update", dto.getAccessRequestId());
            throw new VirtualAccessAlreadyExistsException(dto.getAccessRequestId());
        }
    }


    @Transactional
    public void deleteById(Long id) {
        log.info("Attempting to delete virtual access with ID {}", id);
        VirtualAccess va = virtualAccessRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Virtual access not found for deletion with ID {}", id);
                    return new VirtualAccessNotFoundException(id);
                });

        AccessRequest request = va.getAccessRequest();
        if (request != null) {
            log.debug("Unlinking virtual access from access request ID {}", request.getId());
            request.setVirtualAccess(null);
        }

        virtualAccessRepository.delete(va);
        log.info("Virtual access with ID {} deleted successfully", id);
    }

    @Transactional
    public VirtualAccessDTO createVirtualAccessForRequest(AccessRequest request) {
        log.info("Generating virtual access for AccessRequest ID {}", request.getId());

        if (request.getVirtualAccess() != null) {
            log.warn("Virtual access already exists for AccessRequest ID {}", request.getId());
            throw new IllegalStateException("VirtualAccess already exists for this request.");
        }

        String username = generateUsername(request);
        String rawPassword = generateRandomPassword();
        String encryptedPassword = passwordEncoder.encode(rawPassword);

        VirtualAccess va = new VirtualAccess(username, encryptedPassword, request);
        request.setVirtualAccess(va);
        virtualAccessRepository.save(va);

        log.info("Virtual access created for user '{}'", username);
        emailService.sendVirtualAccessCredentials(request.getUser().getEmail(), username, rawPassword);

        return VirtualAccessMapper.toDTO(va);
    }

    private void validate(VirtualAccessDTO dto) {
        if (dto.getAccessRequestId() == null) {
            throw new MissingAccessRequestIdException();
        }
        if (dto.getUsername() == null || dto.getUsername().isBlank()) {
            log.warn("Validation failed: username is empty.");
            throw new InvalidInputException("Username cannot be empty.");
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            log.warn("Validation failed: password is empty.");
            throw new InvalidInputException("Password cannot be empty.");
        }
    }

    private AccessRequest getAccessRequestOrThrow(Long id) {
        return accessRequestRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("AccessRequest not found with ID {}", id);
                    return new InvalidInputException("AccessRequest not found with ID: " + id);
                });
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
