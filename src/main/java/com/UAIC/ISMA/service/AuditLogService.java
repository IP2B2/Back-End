package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dto.AuditLogDTO;
import com.UAIC.ISMA.entity.AuditLog;
import com.UAIC.ISMA.entity.User;
import com.UAIC.ISMA.exception.AuditLogNotFoundException;
import com.UAIC.ISMA.exception.EntityNotFoundException;
import com.UAIC.ISMA.mapper.AuditLogMapper;
import com.UAIC.ISMA.repository.AuditLogRepository;
import com.UAIC.ISMA.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;
    private final UserRepository userRepository;
    private final AuditLogMapper auditLogMapper;

    @Autowired
    public AuditLogService(AuditLogRepository auditLogRepository,
                           UserRepository userRepository,
                           AuditLogMapper auditLogMapper) {
        this.auditLogRepository = auditLogRepository;
        this.userRepository = userRepository;
        this.auditLogMapper = auditLogMapper;
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

    public AuditLogDTO searchById(Long id) {
        return findById(id);
    }

    public List<AuditLogDTO> searchByAction(String keyword) {
        return auditLogRepository.findByActionContainingIgnoreCase(keyword)
                .stream()
                .map(auditLogMapper::toDto)
                .collect(Collectors.toList());
    }

    public AuditLogDTO create(AuditLogDTO dto) {
        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + dto.getUserId()));

        AuditLog entity = auditLogMapper.toEntity(dto, user);
        AuditLog saved = auditLogRepository.save(entity);
        return auditLogMapper.toDto(saved);
    }

    public AuditLogDTO update(Long id, AuditLogDTO dto) {
        AuditLog existing = auditLogRepository.findById(id)
                .orElseThrow(() -> new AuditLogNotFoundException(id));

        User user = userRepository.findById(dto.getUserId())
                .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + dto.getUserId()));

        existing.setAction(dto.getAction());
        existing.setDetails(dto.getDetails());
        existing.setTimestamp(dto.getTimestamp());
        existing.setUser(user);

        AuditLog updated = auditLogRepository.save(existing);
        return auditLogMapper.toDto(updated);
    }

    public void delete(Long id) {
        AuditLog auditLog = auditLogRepository.findById(id)
                .orElseThrow(() -> new AuditLogNotFoundException(id));
        auditLogRepository.delete(auditLog);
    }
}