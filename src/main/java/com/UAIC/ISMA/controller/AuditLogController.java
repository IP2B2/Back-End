package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.AuditLogDTO;
import com.UAIC.ISMA.exception.AuditLogNotFoundException;
import com.UAIC.ISMA.exception.UserNotFoundException;
import com.UAIC.ISMA.service.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/audit-logs")
@Tag(name = "Audit Logs", description = "Operations related to audit logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    @Operation(summary = "Get all audit logs")
    public ResponseEntity<List<AuditLogDTO>> getAllAuditLogs() {
        log.info("Fetching all audit logs");
        return ResponseEntity.ok(auditLogService.findAll());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get audit log by ID")
    public ResponseEntity<AuditLogDTO> getAuditLogById(
            @Parameter(description = "Audit log ID") @PathVariable Long id) {
        log.info("Fetching audit log with id: {}", id);
        return ResponseEntity.ok(auditLogService.findById(id));
    }

    @GetMapping("/search")
    @Operation(summary = "Search audit logs by action keyword")
    public ResponseEntity<List<AuditLogDTO>> searchByAction(@RequestParam String keyword) {
        log.info("Searching audit logs with keyword: {}", keyword);
        return ResponseEntity.ok(auditLogService.searchByAction(keyword));
    }

    @PostMapping
    @Operation(summary = "Create a new audit log")
    public ResponseEntity<AuditLogDTO> createAuditLog(@RequestBody AuditLogDTO auditLogDTO) {
        log.info("Creating audit log for userId: {}", auditLogDTO.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(auditLogService.create(auditLogDTO));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing audit log")
    public ResponseEntity<AuditLogDTO> updateAuditLog(@PathVariable Long id,
                                                      @RequestBody AuditLogDTO auditLogDTO) {
        log.info("Updating audit log id: {}", id);
        return ResponseEntity.ok(auditLogService.update(id, auditLogDTO));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an audit log")
    public ResponseEntity<Void> deleteAuditLog(@PathVariable Long id) {
        log.info("Deleting audit log with id: {}", id);
        auditLogService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(AuditLogNotFoundException.class)
    public ResponseEntity<String> handleAuditLogNotFoundException(AuditLogNotFoundException ex) {
        log.error("Audit log not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        log.error("User not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
