package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.EquipmentDTO;
import com.UAIC.ISMA.dto.LaboratoryDTO;
import com.UAIC.ISMA.service.LaboratoryService;
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
@RequestMapping("/laboratories")
@Tag(name = "Laboratories", description = "Operations related to laboratories")
public class LaboratoryController {

    private static final Logger logger = LogManager.getLogger(LaboratoryController.class);

    private final LaboratoryService laboratoryService;

    public LaboratoryController(LaboratoryService laboratoryService) {
        this.laboratoryService = laboratoryService;
    }

    @GetMapping
    @Operation(summary = "Get all laboratories", description = "Returns a list of all laboratories.")
    public ResponseEntity<List<LaboratoryDTO>> getAllLaboratories() {
        logger.info("Fetching all laboratories");
        List<LaboratoryDTO> labs = laboratoryService.getAlLaboratories();
        return ResponseEntity.ok(labs);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get laboratory by ID", description = "Returns a single laboratory by its unique ID.")
    public ResponseEntity<LaboratoryDTO> getLaboratoryById(@Parameter(description = "Laboratory ID") @PathVariable long id) {
        logger.info("Fetching laboratory with ID={}", id);
        LaboratoryDTO laboratoryDTO = laboratoryService.getLaboratoryById(id);
        return ResponseEntity.ok(laboratoryDTO);
    }

    @GetMapping("/{id}/equipment")
    @Operation(
            summary = "Get all equipments assigned to a laboratory",
            description = "Retrieves a list of all equipment entities that are assigned to the laboratory with the specified ID."
    )
    public ResponseEntity<List<EquipmentDTO>> getEquipmentForLaboratory(@Parameter(description = "Laboratory ID") @PathVariable long id) {
        logger.info("Fetching equipment for laboratory with ID={}", id);
        List<EquipmentDTO> equipmentList = laboratoryService.getEquipmentByLaboratoryId(id);
        return ResponseEntity.ok(equipmentList);
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Create a new laboratory", description = "Creates a new laboratory with the provided details.")
    public ResponseEntity<LaboratoryDTO> createLaboratory(
            @Parameter(description = "Laboratory data to create")
            @Valid @RequestBody LaboratoryDTO laboratoryDTO) {
        logger.info("Creating new laboratory with name='{}'", laboratoryDTO.getLabName());
        LaboratoryDTO created = laboratoryService.createLaboratory(laboratoryDTO);
        logger.debug("Created laboratory with ID={}", created.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Update an existing laboratory", description = "Updates the laboratory with the specified ID.")
    public ResponseEntity<LaboratoryDTO> updateLaboratory(
            @Parameter(description = "Laboratory ID") @PathVariable Long id,
            @Parameter(description = "Updated laboratory data") @Valid @RequestBody LaboratoryDTO laboratoryDTO) {
        logger.info("Updating laboratory with ID={}", id);
        LaboratoryDTO updated = laboratoryService.updateLaboratory(id, laboratoryDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    @Operation(summary = "Delete a laboratory", description = "Deletes the laboratory with the specified ID.")
    public ResponseEntity<Void> deleteLaboratory(@Parameter(description = "Laboratory ID") @PathVariable Long id) {
        logger.info("Deleting laboratory with ID={}", id);
        laboratoryService.deleteLaboratory(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    @Operation(summary = "Search laboratories", description = "Search laboratories using optional filters: name (partial match) and location. Supports pagination.")
    public ResponseEntity<?> searchLaboratories(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String location,
            Pageable pageable) {
        logger.info("Searching laboratories with name='{}' and location='{}'", name, location);
        return ResponseEntity.ok(laboratoryService.searchLaboratories(name, location, pageable));
    }
}
