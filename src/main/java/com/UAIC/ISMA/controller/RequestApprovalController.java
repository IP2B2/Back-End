package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.RequestApprovalDTO;
import com.UAIC.ISMA.exception.*;
import com.UAIC.ISMA.service.RequestApprovalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
            @Parameter(description = "Optional approver ID and/or access request ID to filter request approvals")
            @RequestParam(required = false) Long approverId,
            @RequestParam(required = false) Long accessRequestId) {
        logger.info("Fetching all request approvals. ApproverId: {}, AccessRequestId: {}", approverId, accessRequestId);
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
        logger.info("Fetching request approval with id: {}", id);
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
        logger.info("Creating new request approval: {}", requestApprovalDTO);
        RequestApprovalDTO createdRequestApproval = requestApprovalService.create(requestApprovalDTO);
        logger.debug("Created request approval with ID {}", createdRequestApproval.getId());
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
        logger.info("Updating request approval with id: {}", id);
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
        logger.info("Deleting request approval with id: {}", id);
        requestApprovalService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(RequestApprovalNotFoundException.class)
    public ResponseEntity<String> handleRequestApprovalNotFoundException(RequestApprovalNotFoundException ex) {
        logger.warn("RequestApprovalNotFoundException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        logger.warn("UserNotFoundException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(AccessRequestNotFoundException.class)
    public ResponseEntity<String> handleAccessRequestNotFoundException(AccessRequestNotFoundException ex) {
        logger.warn("AccessRequestNotFoundException: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<String> handleInvalidInputException(InvalidInputException ex) {
        logger.warn("Bad request: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        logger.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred.");
    }
}
