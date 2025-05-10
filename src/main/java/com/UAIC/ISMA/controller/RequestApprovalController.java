package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.RequestApprovalDTO;
import com.UAIC.ISMA.exception.*;
import com.UAIC.ISMA.service.RequestApprovalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/request-approvals")
@Tag(name = "RequestApprovals", description = "Operations related to request approvals")
public class RequestApprovalController {

    private final RequestApprovalService requestApprovalService;

    public RequestApprovalController(RequestApprovalService requestApprovalService) {
        this.requestApprovalService = requestApprovalService;
    }

    @GetMapping("/all")
    @Operation(
            summary = "Get all request approvals",
            description = "Returns a list of all request approvals. Optionally, filter by approver ID and/or access request ID."
    )
    public ResponseEntity<List<RequestApprovalDTO>> getAllRequestApprovals(
            @Parameter(description = "Optional approver ID and/or access request ID to filter request approvals")
            @RequestParam(required = false) Long approverId,
            @RequestParam(required = false) Long accessRequestId) {
        List<RequestApprovalDTO> requestApprovals = requestApprovalService.findAll(approverId, accessRequestId);
        return ResponseEntity.ok(requestApprovals);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get request approval by ID",
            description = "Returns a single request approval by its unique ID."
    )
    public ResponseEntity<RequestApprovalDTO> getRequestApprovalById(
            @Parameter(description = "Request approval ID")
            @PathVariable Long id) {
        RequestApprovalDTO requestApprovalDTO = requestApprovalService.findById(id);
        return ResponseEntity.ok(requestApprovalDTO);
    }

    @PostMapping
    @Operation(
            summary = "Create a new request approval",
            description = "Creates a new request approval with the provided details."
    )
    public ResponseEntity<RequestApprovalDTO> createRequestApproval(
            @Parameter(description = "Request approval data to create")
            @RequestBody RequestApprovalDTO requestApprovalDTO) {
        RequestApprovalDTO createdRequestApproval = requestApprovalService.create(requestApprovalDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRequestApproval);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing request approval",
            description = "Updates the request approval with the specified ID."
    )
    public ResponseEntity<RequestApprovalDTO> updateRequestApproval(
            @Parameter(description = "Request approval ID")
            @PathVariable Long id,
            @Parameter(description = "Updated request approval data")
            @RequestBody RequestApprovalDTO requestApprovalDTO) {
        RequestApprovalDTO updatedRequestApproval = requestApprovalService.update(id, requestApprovalDTO);
        return ResponseEntity.ok(updatedRequestApproval);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a request approval",
            description = "Deletes the request approval with the specified ID."
    )
    public ResponseEntity<Void> deleteRequestApproval(
            @Parameter(description = "Request approval ID")
            @PathVariable Long id) {
        requestApprovalService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(RequestApprovalNotFoundException.class)
    public ResponseEntity<String> handleRequestApprovalNotFoundException(RequestApprovalNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(AccessRequestNotFoundException.class)
    public ResponseEntity<String> handleAccessRequestNotFoundException(AccessRequestNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
