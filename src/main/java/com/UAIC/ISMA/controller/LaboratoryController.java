package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.LaboratoryDTO;
import com.UAIC.ISMA.exception.LaboratoryNotFoundException;
import com.UAIC.ISMA.exception.UserNotFoundException;
import com.UAIC.ISMA.service.LaboratoryService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/laboratories")
@Tag(name = "Laboratories", description = "Operations related to laboratories")
public class LaboratoryController {

    private final LaboratoryService laboratoryService;

    public LaboratoryController(LaboratoryService laboratoryService) {
        this.laboratoryService = laboratoryService;
    }

    @GetMapping("/all")
    @Operation(
            summary = "Get all laboratories",
            description = "Returns a list of all laboratories."
    )
    public ResponseEntity<List<LaboratoryDTO>> getAllLaboratories() {
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
            @RequestBody LaboratoryDTO laboratoryDTO) {
        LaboratoryDTO created = laboratoryService.createLaboratory(laboratoryDTO);
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
            @RequestBody LaboratoryDTO laboratoryDTO) {
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
        laboratoryService.deleteLaboratory(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(LaboratoryNotFoundException.class)
    public ResponseEntity<String> handleLaboratoryNotFoundException(LaboratoryNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
