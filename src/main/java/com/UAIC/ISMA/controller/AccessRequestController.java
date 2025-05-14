package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.AccessRequestDTO;
import com.UAIC.ISMA.exception.AccessRequestNotFoundException;
import com.UAIC.ISMA.exception.UserNotFoundException;
import com.UAIC.ISMA.repository.UserRepository;
import com.UAIC.ISMA.service.AccessRequestService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import com.UAIC.ISMA.dao.enums.RequestStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDate;

@RestController
@RequestMapping("/access-requests")
@Tag(name = "AccessRequests", description = "Operations related to access requests")
public class AccessRequestController {

    private final AccessRequestService accessRequestService;
    private final UserRepository userRepository;

    public AccessRequestController(AccessRequestService accessRequestService, UserRepository userRepository) {
        this.accessRequestService = accessRequestService;
        this.userRepository = userRepository;
    }


    /*public AccessRequestController(AccessRequestService accessRequestService) {
        this.accessRequestService = accessRequestService;
    }*/

    @Operation(
            summary = "Get all access requests",
            description = "Returns a list of all access requests"
    )
    @GetMapping("/all")
    public ResponseEntity<List<AccessRequestDTO>> getAllAccessRequests() {
        List<AccessRequestDTO> accessRequests = accessRequestService.findAll();
        return ResponseEntity.ok(accessRequests);
    }

    @Operation(
            summary = "Get access request by ID",
            description = "Returns a single access request by its unique ID"
    )
    @GetMapping("/{id}")
    public ResponseEntity<AccessRequestDTO> getAccessRequestById(
            @Parameter(description = "Access request ID")
            @PathVariable Long id) {
        AccessRequestDTO accessRequestDTO = accessRequestService.findById(id);
        return ResponseEntity.ok(accessRequestDTO);
    }

    @Operation(
            summary = "Create a new access request",
            description = "Creates a new access request with the provided details"
    )
    @PostMapping
    public ResponseEntity<AccessRequestDTO> createAccessRequest(
            @Parameter(description = "Access request data to create")
            @Valid @RequestBody AccessRequestDTO dto) {
        AccessRequestDTO createdRequest = accessRequestService.create(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdRequest);
    }

    @Operation(
            summary = "Update an existing access request",
            description = "Updates the access request with the specified ID"
    )
    @PutMapping("/{id}")
    public ResponseEntity<AccessRequestDTO> updateAccessRequest(
            @Parameter(description = "Access request ID")
            @PathVariable Long id,
            @Parameter(description = "Updated access request data")
            @Valid @RequestBody AccessRequestDTO dto) {
        AccessRequestDTO updatedRequest = accessRequestService.update(id, dto);
        return ResponseEntity.ok(updatedRequest);
    }

    @Operation(
            summary = "Partially update an access request",
            description = "Updates only the specified fields of an access request"
    )
    @PatchMapping("/{id}")
    public ResponseEntity<AccessRequestDTO> updatePartialAccessRequest(
            @Parameter(description = "Access request ID")
            @PathVariable Long id,
            @Parameter(description = "Fields to update")
            @RequestBody Map<String, Object> updates) {
        AccessRequestDTO updatedRequest = accessRequestService.updatePartial(id, updates);
        return ResponseEntity.ok(updatedRequest);
    }

    @Operation(
            summary = "Delete an access request",
            description = "Deletes the access request with the specified ID"
    )
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccessRequest(
            @Parameter(description = "Access request ID")
            @PathVariable Long id) {
        accessRequestService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(AccessRequestNotFoundException.class)
    public ResponseEntity<String> handleAccessRequestNotFoundException(AccessRequestNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @GetMapping("/me")
    public ResponseEntity<List<AccessRequestDTO>> getMyAccessRequests(
            @AuthenticationPrincipal UserDetails currentUser,
            @RequestParam(required = false) RequestStatus status,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Long userId = userRepository.findByUsername(currentUser.getUsername())
                .orElseThrow(() -> new UserNotFoundException(0L))
                .getId();

        List<AccessRequestDTO> results = accessRequestService.findByUserWithFilters(userId, status, date, page, size);
        return ResponseEntity.ok(results);
    }

}
