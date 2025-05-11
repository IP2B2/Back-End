package com.UAIC.ISMA.controller;


import com.UAIC.ISMA.dto.EquipmentDTO;
import com.UAIC.ISMA.exception.EquipmentNotFoundException;
import com.UAIC.ISMA.exception.LaboratoryNotFoundException;
import com.UAIC.ISMA.service.EquipmentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipment")
@Tag(name = "Equipment", description = "Operations related to laboratory equipment")
public class EquipmentController {

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
            @RequestBody EquipmentDTO equipmentDTO) {
        EquipmentDTO created = equipmentService.createEquipment(equipmentDTO);
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
            @RequestBody EquipmentDTO equipmentDTO) {
        EquipmentDTO updated = equipmentService.updateEquipment(equipmentDTO, id);
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
        equipmentService.deleteEquipment(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(EquipmentNotFoundException.class)
    public ResponseEntity<String> handleEquipmentNotFoundException(EquipmentNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(LaboratoryNotFoundException.class)
    public ResponseEntity<String> handleLaboratoryNotFoundException(LaboratoryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
