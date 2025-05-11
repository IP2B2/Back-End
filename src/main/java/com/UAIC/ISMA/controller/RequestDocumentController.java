package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.RequestDocumentDTO;
import com.UAIC.ISMA.exception.RequestDocumentNotFoundException;
import com.UAIC.ISMA.service.RequestDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/request-documents")
@Tag(name = "RequestDocuments", description = "Operations related to request documents")
public class RequestDocumentController {

    private final RequestDocumentService requestDocumentService;

    public RequestDocumentController(RequestDocumentService requestDocumentService) {
        this.requestDocumentService = requestDocumentService;
    }

    @GetMapping("/all")
    @Operation(
            summary = "Get all request documents",
            description = "Returns a list of all documents. Optionally, filter by userId."
    )
    public ResponseEntity<List<RequestDocumentDTO>> getAllDocuments(
            @Parameter (description = "Optional user ID to filter requestDocument")
            @RequestParam ( required = false) Long userId,
            @Parameter (description = "Optional access request ID to filter requestDocument")
            @RequestParam ( required = false ) Long accessRequestId
    ) {
       List<RequestDocumentDTO> requestDocuments = requestDocumentService.findAll(userId, accessRequestId);
       return ResponseEntity.ok (requestDocuments);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get request document by ID",
            description = "Returns a single document by its unique ID."
    )
    public ResponseEntity<RequestDocumentDTO> getById(
            @Parameter(description = "Document ID")
            @PathVariable Long id
    ) {
        return ResponseEntity.ok(requestDocumentService.findById(id));
    }

    @PostMapping
    @Operation(
            summary = "Create a new request document",
            description = "Creates a new document with the provided details."
    )
    public ResponseEntity<RequestDocumentDTO> create(
            @Parameter (description = "RequestDocument  data to create")
            @RequestBody RequestDocumentDTO dto
    ) {
        return ResponseEntity.ok(requestDocumentService.create(dto));
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a request document",
            description = "Updates the document with the specified ID."
    )
    public ResponseEntity<RequestDocumentDTO> update(
            @Parameter(description = "Document ID")
            @PathVariable Long id,
            @Parameter(description = "Updated document data")
            @RequestBody RequestDocumentDTO dto
    ) {
        return ResponseEntity.ok(requestDocumentService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a request document",
            description = "Deletes the document with the specified ID."
    )
    public ResponseEntity<Void> delete(
            @Parameter(description = "Notification ID")
            @PathVariable Long id
    ) {
        requestDocumentService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(RequestDocumentNotFoundException.class)
    public ResponseEntity<String> handleNotFound(RequestDocumentNotFoundException ex) {
        return ResponseEntity.status(404).body(ex.getMessage());
    }
}
