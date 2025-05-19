package com.UAIC.ISMA.controller;


import com.UAIC.ISMA.dto.EquipmentDTO;
import com.UAIC.ISMA.exception.EquipmentNotFoundException;
import com.UAIC.ISMA.exception.InvalidInputException;
import com.UAIC.ISMA.exception.LaboratoryNotFoundException;
import com.UAIC.ISMA.service.EquipmentService;
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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/equipment")
@Tag(name = "Equipment", description = "Operations related to laboratory equipment")
public class EquipmentController {

    private static final Logger logger = LogManager.getLogger(EquipmentController.class);

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping("/all")
    @Operation(
            summary = "Get all equipments",
            description = "Returns a list of all equipments. Optionally filter by laboratoryId."
    )
    public ResponseEntity<List<EquipmentDTO>> getAllEquipment(
            @Parameter(description = "Optional laboratory ID to filter equipment")
            @RequestParam(required = false) Long laboratoryId) {
        logger.info("Fetching all equipments");
        List<EquipmentDTO> equipments = equipmentService.getAllEquipments(laboratoryId);
        return ResponseEntity.ok(equipments);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get equipment by ID",
            description = "Returns a single equipment item by its unique ID."
    )
    public ResponseEntity<EquipmentDTO> getEquipmentById(
            @Parameter(description = "Equipment ID")
            @PathVariable Long id) {
        logger.info("Fetching equipment with ID={}", id);
        EquipmentDTO equipment = equipmentService.getEquipmentById(id);
        return ResponseEntity.ok(equipment);
    }

    @PostMapping
    @Operation(
            summary = "Create a new equipment item",
            description = "Creates a new equipment item with the provided details."
    )
    public ResponseEntity<EquipmentDTO> createEquipment(
            @Parameter(description = "Equipment data to create")
            @RequestBody @Valid EquipmentDTO equipmentDTO) {
        logger.info("Creating a new equipment with name='{}'", equipmentDTO.getName());
        EquipmentDTO created = equipmentService.createEquipment(equipmentDTO);
        logger.info("Created equipment with name='{}'", created.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a existing equipment item",
            description = "Updates the equipment item with the specified ID."
    )
    public ResponseEntity<EquipmentDTO> updateEquipment(
            @Parameter(description = "Equipment ID")
            @PathVariable Long id,
            @Parameter(description = "Updated equipment data")
            @RequestBody @Valid EquipmentDTO equipmentDTO) {
        logger.info("Updating equipment with ID={}", id);
        EquipmentDTO updated = equipmentService.updateEquipment(equipmentDTO, id);
        logger.info("Updated equipment with ID={}", updated.getId());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete equipment",
            description = "Deletes the equipment item with the specified ID."
    )
    public ResponseEntity<Void> deleteEquipment(
            @Parameter(description = "Equipment ID")
            @PathVariable Long id) {
        logger.info("Deleting equipment with ID={}", id);
        equipmentService.deleteEquipment(id);
        logger.info("Deleted equipment with ID={}", id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(
            summary = "Search equipment",
            description = "Search equipment using optional filters: name (partial match), availability status, and laboratory ID. Supports pagination."
    )
    public ResponseEntity<?> searchEquipment(
            @Parameter(description = "Optional name to search (partial match)")
            @RequestParam(required = false) String name,

            @Parameter(description = "Optional availability status (e.g., AVAILABLE, IN_USE)")
            @RequestParam(required = false) String status,

            @Parameter(description = "Optional laboratory ID to filter")
            @RequestParam(required = false) Long labId,

            @Parameter(description = "Pagination parameters (page, size, sort)")
            Pageable pageable
    ) {
        logger.info("Searching equipment with name='{}', status='{}', labId='{}'", name, status, labId);
        return ResponseEntity.ok(equipmentService.searchEquipment(name, status, labId, pageable));
    }


    @ExceptionHandler(EquipmentNotFoundException.class)
    public ResponseEntity<String> handleEquipmentNotFoundException(EquipmentNotFoundException ex) {
        logger.warn("Equipment not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(LaboratoryNotFoundException.class)
    public ResponseEntity<String> handleLaboratoryNotFoundException(LaboratoryNotFoundException ex) {
        logger.warn("Laboratory not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(InvalidInputException.class)
    public ResponseEntity<String> handleInvalidInput(InvalidInputException ex) {
        logger.warn("Invalid input: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
        logger.warn("Validation failed for method argument");
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage())
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
    }
}
