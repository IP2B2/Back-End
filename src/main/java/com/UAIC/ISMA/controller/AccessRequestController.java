package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.config.UserDetailsImpl;
import com.UAIC.ISMA.dto.AccessRequestDTO;
import com.UAIC.ISMA.entity.User;
import com.UAIC.ISMA.entity.enums.RequestStatus;
import com.UAIC.ISMA.entity.enums.RoleName;
import com.UAIC.ISMA.service.AccessRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.EnumSet;

@RestController
@RequestMapping("/access-requests")
@Tag(name = "AccessRequests", description = "Operations related to access requests")
public class AccessRequestController {

    private static final Logger logger = LogManager.getLogger(AccessRequestController.class);
    private final AccessRequestService accessRequestService;

    public AccessRequestController(AccessRequestService accessRequestService) {
        this.accessRequestService = accessRequestService;
    }

    @Operation(summary = "Get all access requests", description = "Returns a list of all access requests")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORDONATOR')")
    @GetMapping
    public ResponseEntity<List<AccessRequestDTO>> getAllAccessRequests(
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        RoleName role = userDetails.getUser().getRole().getRoleName();
        if (!EnumSet.of(RoleName.ADMIN, RoleName.COORDONATOR).contains(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        logger.info("Fetching all access requests");
        List<AccessRequestDTO> accessRequests = accessRequestService.findAll();
        return ResponseEntity.ok(accessRequests);
    }


    @Operation(summary = "Get access request by ID", description = "Returns a single access request by its unique ID")
    @GetMapping("/{id}")
    public ResponseEntity<AccessRequestDTO> getAccessRequestById(
            @Parameter(description = "Access request ID") @PathVariable Long id) {
        logger.info("Fetching access request with ID {}", id);
        AccessRequestDTO accessRequestDTO = accessRequestService.findById(id);
        return ResponseEntity.ok(accessRequestDTO);
    }

    @Operation(summary = "Create a new access request", description = "Creates a new access request with the provided details")
    @PreAuthorize("hasAnyAuthority('RESEARCHER', 'STUDENT')")
    @PostMapping
    public ResponseEntity<AccessRequestDTO> createAccessRequest(
            @Parameter(description = "Access request data to create") @Valid @RequestBody AccessRequestDTO dto) {
        logger.info("Creating new access request for user {}", dto.getUserId());
        AccessRequestDTO createdRequest = accessRequestService.create(dto);
        logger.debug("Created access request with ID {}", createdRequest.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
    }

    @Operation(summary = "Update an existing access request", description = "Updates the access request with the specified ID")
    @PutMapping("/{id}")
    public ResponseEntity<AccessRequestDTO> updateAccessRequest(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id,
            @Valid @RequestBody AccessRequestDTO dto) {

        // 🟢 Adăugăm verificarea manuală a rolului
        RoleName role = userDetails.getUser().getRole().getRoleName();
        if (!EnumSet.of(RoleName.ADMIN, RoleName.COORDONATOR).contains(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        AccessRequestDTO updatedRequest = accessRequestService.update(id, dto);
        return ResponseEntity.ok(updatedRequest);
    }


    @Operation(summary = "Partially update an access request", description = "Updates only the specified fields of an access request")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORDONATOR')")
    @PatchMapping("/{id}")
    public ResponseEntity<AccessRequestDTO> updatePartialAccessRequest(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {

        RoleName role = userDetails.getUser().getRole().getRoleName();
        if (!EnumSet.of(RoleName.ADMIN, RoleName.COORDONATOR).contains(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        logger.info("Partially updating access request with ID {}", id);
        AccessRequestDTO updatedRequest = accessRequestService.updatePartial(id, updates);
        return ResponseEntity.ok(updatedRequest);
    }



    @Operation(summary = "Delete an access request", description = "Deletes the access request with the specified ID")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORDONATOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccessRequest(
            @AuthenticationPrincipal UserDetailsImpl userDetails,
            @PathVariable Long id) {

        RoleName role = userDetails.getUser().getRole().getRoleName();
        if (!EnumSet.of(RoleName.ADMIN, RoleName.COORDONATOR).contains(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        logger.info("Deleting access request with ID {}", id);
        accessRequestService.delete(id);
        return ResponseEntity.noContent().build();
    }


    @PreAuthorize("hasAuthority('COORDONATOR')")
    @PutMapping("/approve/{id}")
    public ResponseEntity<AccessRequestDTO> approveRequest(@PathVariable Long id) {
        AccessRequestDTO approvedRequest = accessRequestService.approveAccessRequest(id);
        return ResponseEntity.ok(approvedRequest);
    }

    @GetMapping("/search")
    public ResponseEntity<?> filterAccessRequests(
            @RequestParam(required = false) RequestStatus status,
            @RequestParam(required = false) String equipmentType,
            @RequestParam(required = false) Long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {

        User user = userDetails.getUser();
        RoleName role = user.getRole().getRoleName();

        if (!EnumSet.of(RoleName.ADMIN, RoleName.COORDONATOR).contains(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN)
                    .body("Only ADMIN and COORDONATOR can filter access requests.");
        }

        Pageable pageable = PageRequest.of(page, size);
        logger.info("Searching access requests with filters: status={}, equipmentType={}, userId={}", status, equipmentType, userId);
        Page<AccessRequestDTO> results = accessRequestService.filterRequests(status, equipmentType, userId, pageable);

        logger.debug("Found {} access requests", results.getTotalElements());
        return ResponseEntity.ok(results);
    }


}
