package com.UAIC.ISMA.service;
import com.UAIC.ISMA.dao.AuditLog;
import com.UAIC.ISMA.dao.User;
import com.UAIC.ISMA.dto.AuditLogDTO;
import com.UAIC.ISMA.exception.AuditLogNotFoundException;
import com.UAIC.ISMA.mapper.AuditLogMapper;
import com.UAIC.ISMA.repository.AuditLogRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import com.UAIC.ISMA.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final AuditLogMapper auditLogMapper;
    private final UserRepository userRepository;

    public AuditLogService(AuditLogRepository auditLogRepository, AuditLogMapper auditLogMapper, UserRepository userRepository) {
        this.auditLogRepository = auditLogRepository;
        this.auditLogMapper = auditLogMapper;
        this.userRepository = userRepository;
    }
    public List<AuditLogDTO> findAll() {
        return auditLogRepository.findAll()
                .stream()
                .map(auditLogMapper::toDto)
                .collect(Collectors.toList());
    }

    public AuditLogDTO findById(Long id) {
        AuditLog auditLog = auditLogRepository.findById(id)
                .orElseThrow(() -> new AuditLogNotFoundException(id));
        return auditLogMapper.toDto(auditLog);
    }

    public AuditLogDTO create(AuditLogDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + dto.getUserId()));
        AuditLog entity = auditLogMapper.toEntity(dto);
        entity.setUser(user);
        AuditLog savedAuditLog = auditLogRepository.save(entity);
        return auditLogMapper.toDto(savedAuditLog);
    }


    public AuditLogDTO update(Long id, AuditLogDTO dto) {
        AuditLog existing = auditLogRepository.findById(id)
                .orElseThrow(() -> new AuditLogNotFoundException(id));

        existing.setAction(dto.getAction());
        existing.setDetails(dto.getDetails());
        existing.setTimestamp(dto.getTimestamp());

        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + dto.getUserId()));
            existing.setUser(user);
        }

        AuditLog updatedAuditLog = auditLogRepository.save(existing);
        return auditLogMapper.toDto(updatedAuditLog);
    }

    public void delete(Long id) {
        AuditLog auditLog = auditLogRepository.findById(id)
                .orElseThrow(() -> new AuditLogNotFoundException(id));
        auditLogRepository.delete(auditLog);
    }
}
