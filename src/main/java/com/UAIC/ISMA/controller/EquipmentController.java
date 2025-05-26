package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.EquipmentDTO;
import com.UAIC.ISMA.service.EquipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/equipment")
@Tag(name = "Equipment", description = "Operations related to laboratory equipment")
public class EquipmentController {

    private static final Logger logger = LogManager.getLogger(EquipmentController.class);
    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping
    @Operation(summary = "Get all equipments", description = "Returns a list of all equipments. Optionally filter by laboratoryId.")
    public ResponseEntity<List<EquipmentDTO>> getAllEquipment(
            @Parameter(description = "Optional laboratory ID to filter equipment")
            @RequestParam(name = "laboratoryId", required = false) Long laboratoryId) {
        logger.info("Fetching all equipments");
        List<EquipmentDTO> equipments = equipmentService.getAllEquipments(laboratoryId);
        return ResponseEntity.ok(equipments);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get equipment by ID", description = "Returns a single equipment item by its unique ID.")
    public ResponseEntity<EquipmentDTO> getEquipmentById(
            @Parameter(description = "Equipment ID") @PathVariable Long id) {
        logger.info("Fetching equipment with ID={}", id);
        EquipmentDTO equipment = equipmentService.getEquipmentById(id);
        return ResponseEntity.ok(equipment);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Create a new equipment item", description = "Creates a new equipment item with the provided details.")
    public ResponseEntity<EquipmentDTO> createEquipment(
            @Parameter(description = "Equipment data to create")
            @RequestBody @Valid EquipmentDTO equipmentDTO) {
        logger.info("Creating a new equipment with name='{}'", equipmentDTO.getName());
        EquipmentDTO created = equipmentService.createEquipment(equipmentDTO);
        logger.info("Created equipment with name='{}'", created.getName());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Update a existing equipment item", description = "Updates the equipment item with the specified ID.")
    public ResponseEntity<EquipmentDTO> updateEquipment(
            @Parameter(description = "Equipment ID") @PathVariable Long id,
            @Parameter(description = "Updated equipment data") @RequestBody @Valid EquipmentDTO equipmentDTO) {
        logger.info("Updating equipment with ID={}", id);
        EquipmentDTO updated = equipmentService.updateEquipment(equipmentDTO, id);
        logger.info("Updated equipment with ID={}", updated.getId());
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    @Operation(summary = "Delete equipment", description = "Deletes the equipment item with the specified ID.")
    public ResponseEntity<Void> deleteEquipment(
            @Parameter(description = "Equipment ID") @PathVariable Long id) {
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
            @Parameter(description = "Optional name to search (partial match)") @RequestParam(name = "name", required = false) String name,
            @Parameter(description = "Optional availability status (e.g., AVAILABLE, IN_USE)") @RequestParam(name = "availabilityStatus", required = false) String status,
            @Parameter(description = "Optional laboratory ID to filter") @RequestParam(name = "laboratoryId", required = false) Long labId,
            @Parameter(description = "Pagination parameters (page, size, sort)") Pageable pageable
    ) {
        logger.info("Searching equipment with name='{}', status='{}', labId='{}'", name, status, labId);
        return ResponseEntity.ok(equipmentService.searchEquipment(name, status, labId, pageable));
    }
}
