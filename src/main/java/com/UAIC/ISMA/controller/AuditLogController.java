package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.AuditLogDTO;
import com.UAIC.ISMA.mapper.AuditLogMapper;
import com.UAIC.ISMA.service.AuditLogService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
public class AuditLogController {

    private final AuditLogService auditLogService;
    private final AuditLogMapper auditLogMapper;

    public AuditLogController(AuditLogService auditLogService, AuditLogMapper auditLogMapper) {
        this.auditLogService = auditLogService;
        this.auditLogMapper = auditLogMapper;
    }

    @PostMapping
    public ResponseEntity<AuditLogDTO> create(@RequestBody AuditLogDTO dto) {
        var saved = auditLogService.create(auditLogMapper.toEntity(dto));
        return ResponseEntity.ok(auditLogMapper.toDto(saved));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuditLogDTO> read(@PathVariable Long id) {
        var auditLog = auditLogService.read(id);
        return ResponseEntity.ok(auditLogMapper.toDto(auditLog));
    }

    @GetMapping
    public ResponseEntity<List<AuditLogDTO>> readAll() {
        var auditLogs = auditLogService.readAll()
                .stream()
                .map(auditLogMapper::toDto)
                .toList();
        return ResponseEntity.ok(auditLogs);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuditLogDTO> update(@PathVariable Long id, @RequestBody AuditLogDTO dto) {
        var updated = auditLogService.update(id, auditLogMapper.toEntity(dto));
        return ResponseEntity.ok(auditLogMapper.toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        auditLogService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
