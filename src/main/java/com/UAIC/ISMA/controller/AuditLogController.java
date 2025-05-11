package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.AuditLogDTO;
import com.UAIC.ISMA.exception.AuditLogNotFoundException;
import com.UAIC.ISMA.exception.UserNotFoundException;
import com.UAIC.ISMA.service.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@Tag(name = "Audit Logs", description = "Operations related to audit logs")
public class AuditLogController {

    private final AuditLogService auditLogService;

    public AuditLogController(AuditLogService auditLogService) {
        this.auditLogService = auditLogService;
    }

    @GetMapping
    @Operation(summary = "Get all audit logs",
            description = "Returns a list of all audit logs")
    public ResponseEntity<List<AuditLogDTO>> getAllAuditLogs() {
        List<AuditLogDTO> auditLogs = auditLogService.findAll();
        return ResponseEntity.ok(auditLogs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get audit log by ID",
            description = "Returns a single audit log by its ID")
    public ResponseEntity<AuditLogDTO> getAuditLogById(
            @Parameter(description = "Audit log ID")
            @PathVariable Long id) {
        AuditLogDTO auditLogDTO = auditLogService.findById(id);
        return ResponseEntity.ok(auditLogDTO);
    }

    @PostMapping
    @Operation(summary = "Create a new audit log",
            description = "Creates a new audit log with the provided details",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Audit log created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input or user not found")
            })
    public ResponseEntity<AuditLogDTO> createAuditLog(
            @Parameter(description = "Audit log data to create")
            @RequestBody AuditLogDTO auditLogDTO) {
        AuditLogDTO createdAuditLog = auditLogService.create(auditLogDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdAuditLog);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an existing audit log",
            description = "Updates the audit log with the specified ID")
    public ResponseEntity<AuditLogDTO> updateAuditLog(
            @Parameter(description = "Audit log ID")
            @PathVariable Long id,
            @Parameter(description = "Updated audit log data")
            @RequestBody AuditLogDTO auditLogDTO) {
        AuditLogDTO updatedAuditLog = auditLogService.update(id, auditLogDTO);
        return ResponseEntity.ok(updatedAuditLog);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an audit log",
            description = "Deletes the audit log with the specified ID")
    public ResponseEntity<Void> deleteAuditLog(
            @Parameter(description = "Audit log ID")
            @PathVariable Long id) {
        auditLogService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(AuditLogNotFoundException.class)
    public ResponseEntity<String> handleAuditLogNotFoundException(AuditLogNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}

