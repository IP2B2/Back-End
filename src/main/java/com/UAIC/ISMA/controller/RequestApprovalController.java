package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.RequestApprovalDTO;
import com.UAIC.ISMA.service.RequestApprovalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/request-approvals")
@Tag(name = "RequestApprovals", description = "Operations related to request approvals")
public class RequestApprovalController {

    private static final Logger logger = LogManager.getLogger(RequestApprovalController.class);

    private final RequestApprovalService requestApprovalService;

    public RequestApprovalController(RequestApprovalService requestApprovalService) {
        this.requestApprovalService = requestApprovalService;
    }

    @GetMapping
    @Operation(
            summary = "Get all request approvals",
            description = "Returns a list of all request approvals. Optionally, filter by approver ID and/or access request ID."
    )
    public ResponseEntity<List<RequestApprovalDTO>> getAllRequestApprovals(
            @RequestParam(required = false) Long accessRequestId,
            @RequestParam(required = false) Long approverId) {
        logger.info("Fetching all request approvals. ApproverId: {}, AccessRequestId: {}", approverId, accessRequestId);
        List<RequestApprovalDTO> requestApprovals = requestApprovalService.findAll(accessRequestId, approverId);
        return ResponseEntity.ok(requestApprovals);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get request approval by ID",
            description = "Returns a single request approval by its unique ID."
    )
    public ResponseEntity<RequestApprovalDTO> getRequestApprovalById(@PathVariable Long id) {
        logger.info("Fetching request approval with id: {}", id);
        RequestApprovalDTO requestApprovalDTO = requestApprovalService.findById(id);
        return ResponseEntity.ok(requestApprovalDTO);
    }

    @PostMapping
    @Operation(
            summary = "Create a new request approval",
            description = "Creates a new request approval with the provided details."
    )
    public ResponseEntity<RequestApprovalDTO> createRequestApproval(@RequestBody RequestApprovalDTO requestApprovalDTO) {
        logger.info("Creating new request approval: {}", requestApprovalDTO);
        RequestApprovalDTO createdRequestApproval = requestApprovalService.create(requestApprovalDTO);
        logger.debug("Created request approval with ID {}", createdRequestApproval.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRequestApproval);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORDONATOR')")
    @Operation(
            summary = "Update an existing request approval",
            description = "Updates the request approval with the specified ID."
    )
    public ResponseEntity<RequestApprovalDTO> updateRequestApproval(@PathVariable Long id,
                                                                    @RequestBody RequestApprovalDTO requestApprovalDTO) {
        logger.info("Updating request approval with id: {}", id);
        RequestApprovalDTO updatedRequestApproval = requestApprovalService.update(id, requestApprovalDTO);
        return ResponseEntity.ok(updatedRequestApproval);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORDONATOR')")
    @Operation(
            summary = "Delete a request approval",
            description = "Deletes the request approval with the specified ID."
    )
    public ResponseEntity<Void> deleteRequestApproval(@PathVariable Long id) {
        logger.info("Deleting request approval with id: {}", id);
        requestApprovalService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
