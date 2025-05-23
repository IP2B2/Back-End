package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.LaboratoryDTO;
import com.UAIC.ISMA.exception.InvalidInputException;
import com.UAIC.ISMA.exception.LaboratoryNotFoundException;
import com.UAIC.ISMA.exception.UserNotFoundException;
import com.UAIC.ISMA.service.LaboratoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/laboratories")
@Tag(name = "Laboratories", description = "Operations related to laboratories")
public class LaboratoryController {

    private static final Logger logger = LogManager.getLogger(LaboratoryController.class);


    private final LaboratoryService laboratoryService;

    public LaboratoryController(LaboratoryService laboratoryService) {
        this.laboratoryService = laboratoryService;
    }

    @GetMapping
    @Operation(
            summary = "Get all laboratories",
            description = "Returns a list of all laboratories."
    )
    public ResponseEntity<List<LaboratoryDTO>> getAllLaboratories() {
        logger.info("Fetching all laboratories");
        List<LaboratoryDTO> labs = laboratoryService.getAlLaboratories();
        return ResponseEntity.ok(labs);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get laboratory by ID",
            description = "Returns a single laboratory by its unique ID."
    )
    public ResponseEntity<LaboratoryDTO> getLaboratoryById(
            @Parameter(description = "Laboratpry ID")
            @PathVariable long id) {
        logger.info("Fetching laboratory with ID={}", id);
        LaboratoryDTO laboratoryDTO = laboratoryService.getLaboratoryById(id);
        return ResponseEntity.ok(laboratoryDTO);
    }

    @PostMapping
    @Operation(
            summary = "Create a new laboratory",
            description = "Creates a new laboratory with the provided details."
    )
    public ResponseEntity<LaboratoryDTO> createLaboratory(
            @Parameter(description = "Laboratory data to creat")
            @Valid @RequestBody LaboratoryDTO laboratoryDTO) {
        logger.info("Creating new laboratory with name='{}'", laboratoryDTO.getLabName());
        LaboratoryDTO created = laboratoryService.createLaboratory(laboratoryDTO);

        logger.debug("Created laboratory with ID={}", created.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing laboratory",
            description = "Updates the laboratory with the specified ID."
    )
    public ResponseEntity<LaboratoryDTO> updateLaboratory(
            @Parameter(description = "Laboratory ID")
            @PathVariable Long id,
            @Parameter(description = "Updated laboratory data")
            @Valid @RequestBody LaboratoryDTO laboratoryDTO) {
        logger.info("Updating laboratory with ID={}", id);
        LaboratoryDTO updated = laboratoryService.updateLaboratory(id, laboratoryDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a laboratory",
            description = "Deletes the laboratory with the specified ID."
    )
    public ResponseEntity<Void> deleteLaboratory(
            @Parameter(description = "Laboratory ID")
            @PathVariable Long id) {
        logger.info("Deleting laboratory with ID={}", id);
        laboratoryService.deleteLaboratory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search laboratories",
            description = "Search laboratories using optional filters: name (partial match) and location. Supports pagination."
    )
    public ResponseEntity<?> searchLaboratories(
            @Parameter(description = "Optional name to search (partial match)")
            @RequestParam(required = false) String name,

            @Parameter(description = "Optional location to filter (partial match)")
            @RequestParam(required = false) String location,

            @Parameter(description = "Pagination parameters (page, size, sort)")
            Pageable pageable
    ) {
        logger.info("Searching laboratories with name='{}' and location='{}'", name, location);
        return ResponseEntity.ok(laboratoryService.searchLaboratories(name, location, pageable));
    }

    @ExceptionHandler(LaboratoryNotFoundException.class)
    public ResponseEntity<String> handleLaboratoryNotFoundException(LaboratoryNotFoundException ex) {
        logger.warn("Laboratory not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        logger.warn("User not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<String> handleInvalidInput(InvalidInputException ex) {
        logger.warn("Invalid input: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<String> handleValidationErrors(MethodArgumentNotValidException ex) {
        logger.warn("Validation failed for method argument");
        String errorMsg = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining("; "));
        return ResponseEntity.badRequest().body(errorMsg);
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        logger.error("Unexpected error: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Unexpected error occurred");
    }
}
