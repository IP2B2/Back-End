package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.LabDocumentDTO;
import com.UAIC.ISMA.exception.NotificationNotFoundException;
import com.UAIC.ISMA.exception.UserNotFoundException;
import com.UAIC.ISMA.service.LabDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/labDocs")
@Tag(name = "LabDocument", description = "Operations related to labDocuments")
public class LabDocumentController {
    private final LabDocumentService labDocumentService;

    public LabDocumentController(LabDocumentService labDocumentService) {
        this.labDocumentService = labDocumentService;
    }

    @GetMapping
    @Operation(
            summary = "Get all labDocuments",
            description = "Returns a list of all the labDocuments"
    )
    public ResponseEntity<List<LabDocumentDTO>> getAllLabDocs(@Parameter(description = "Optional user ID to filter labDocuments")
                                                              @RequestParam(required = false) Long labId) {
        List<LabDocumentDTO> labDocumentList = labDocumentService.getAllDocuments();
        return ResponseEntity.ok(labDocumentList);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get labDocument by ID",
            description = "Returns a single labDocument by its unique ID."
    )
    public ResponseEntity<LabDocumentDTO> getLabDocumentsById(
            @Parameter(description = "LabDocument ID")
            @PathVariable Long id) {
        LabDocumentDTO labDocumentDTO = labDocumentService.findById(id);
        return ResponseEntity.ok(labDocumentDTO);
    }


    @GetMapping("/laboratory/{laboratoryId}")
    @Operation(
            summary = "Get labDocument by labID",
            description = "Returns a list of labDocument from a laboratory with a specific ID."
    )
    public ResponseEntity<List<LabDocumentDTO>> getDocsByLabId(
            @Parameter(description = "Laboratory ID")
            @PathVariable("laboratoryId") Long laboratoryId) {
        List<LabDocumentDTO> labDocumentDTOList = labDocumentService.getDocumentsByLabId(laboratoryId);
        return ResponseEntity.ok(labDocumentDTOList);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('Coordinator')")
    @Operation(
            summary = "Create a new labDocument",
            description = "Creates a new labDocument with the provided details."
    )
    public LabDocumentDTO createLabDocument(@Parameter(description = "LabDocument data to create")
                                            @RequestBody LabDocumentDTO labDocumentDTO) {
        return labDocumentService.createDocument(labDocumentDTO);
    }


    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing labDocument",
            description = "Updates the labDocument with the specified ID."
    )
    public ResponseEntity<LabDocumentDTO> updateLabDoc(
            @Parameter(description = "LabDocument ID")
            @PathVariable Long id,
            @Parameter(description = "Updated labDocument data")
            @RequestBody LabDocumentDTO labDocumentDTO) {
        LabDocumentDTO update = labDocumentService.updateLabDocument(id, labDocumentDTO);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a labDocument",
            description = "Deletes the labDocument with the specified ID."
    )
    public ResponseEntity<Void> deleteLabDoc(
            @Parameter(description = "LabDocument ID")
            @PathVariable Long id) {
        labDocumentService.deleteLabDocument(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<String> handleNotificationNotFoundException(NotificationNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
