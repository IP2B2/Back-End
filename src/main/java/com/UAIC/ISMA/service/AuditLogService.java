package com.UAIC.ISMA.service;
import com.UAIC.ISMA.dao.AuditLog;
import com.UAIC.ISMA.repository.AuditLogRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditLogService {

    private final AuditLogRepository auditLogRepository;

    public AuditLogService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    public AuditLog create(AuditLog auditLog) {
        return auditLogRepository.save(auditLog);
    }

    public AuditLog read(Long id) {
        return auditLogRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("AuditLog not found with id " + id));
    }

    public List<AuditLog> readAll() {
        return auditLogRepository.findAll();
    }

    public AuditLog update(Long id, AuditLog updatedAuditLog) {
        AuditLog existing = read(id);
        existing.setAction(updatedAuditLog.getAction());
        existing.setDetails(updatedAuditLog.getDetails());
        existing.setTimestamp(updatedAuditLog.getTimestamp());
        existing.setUser(updatedAuditLog.getUser());
        return auditLogRepository.save(existing);
    }

    public void delete(Long id) {
        if (!auditLogRepository.existsById(id)) {
            throw new EntityNotFoundException("AuditLog not found with id " + id);
        }
        auditLogRepository.deleteById(id);
    }
}
