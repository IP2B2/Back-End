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
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

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
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Get all audit logs",
            description = "Returns a list of all audit logs. Each log contains: ID, action, details, timestamp, and user ID."
    )
    public ResponseEntity<List<AuditLogDTO>> getAllAuditLogs() {
        log.info("Fetching all audit logs");
        return ResponseEntity.ok(auditLogService.findAll());
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Get audit log by ID",
            description = "Returns a single audit log identified by its ID. Includes: ID, action, details, timestamp, and user ID."
    )
    public ResponseEntity<AuditLogDTO> getAuditLogById(
            @Parameter(description = "Audit log ID") @PathVariable Long id) {
        log.info("Fetching audit log with id: {}", id);
        return ResponseEntity.ok(auditLogService.findById(id));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Search audit logs by keyword",
            description = "Searches audit logs by keyword found in the 'action' field. Returns a list of matching logs."
    )
    public ResponseEntity<List<AuditLogDTO>> searchByAction(@RequestParam String keyword) {
        log.info("Searching audit logs with keyword: {}", keyword);
        return ResponseEntity.ok(auditLogService.searchByAction(keyword));
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Create a new audit log",
            description = "Creates a new audit log entry. Requires: action, details, timestamp, and user ID. Returns the created log.",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Audit log created successfully"),
                    @ApiResponse(responseCode = "400", description = "Invalid input or user not found")
            }
    )
    public ResponseEntity<AuditLogDTO> createAuditLog(@Valid @RequestBody AuditLogDTO auditLogDTO) {
        log.info("Creating audit log for userId: {}", auditLogDTO.getUserId());
        return ResponseEntity.status(HttpStatus.CREATED).body(auditLogService.create(auditLogDTO));
    }


    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Update an existing audit log",
            description = "Updates the audit log with the specified ID. Allows updating: action, details, timestamp, and user ID."
    )
    public ResponseEntity<AuditLogDTO> updateAuditLog(@PathVariable Long id,
                                                      @Valid @RequestBody AuditLogDTO auditLogDTO) {
        log.info("Updating audit log id: {}", id);
        return ResponseEntity.ok(auditLogService.update(id, auditLogDTO));
    }


    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(
            summary = "Delete an audit log",
            description = "Deletes the audit log identified by the given ID. Returns no content on success."
    )
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
