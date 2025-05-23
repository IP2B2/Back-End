package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.LabDocumentDTO;
import com.UAIC.ISMA.service.LabDocumentService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/labdocuments")
public class LabDocumentController {

    private final LabDocumentService service;

    public LabDocumentController(LabDocumentService service) {
        this.service = service;
    }

    @GetMapping("/labs/{labId}")
    public ResponseEntity<List<LabDocumentDTO>> getDocumentsByLab(@PathVariable String labId) {
        return ResponseEntity.ok(service.getDocumentsByLab(labId));
    }

    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) {
        return service.downloadDocument(id);
    }

    @PostMapping("/labs/{labId}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COORDINATOR')")
    public ResponseEntity<?> uploadLabDocument(@PathVariable String labId,
                                               @RequestParam("file") MultipartFile file,
                                               @RequestParam(required = false) String version) {
        return ResponseEntity.ok(service.storeDocument(file, labId, null, version));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COORDINATOR')")
    public ResponseEntity<LabDocumentDTO> updateDocument(@PathVariable Long id,
                                                         @RequestParam("file") MultipartFile file,
                                                         @RequestParam(required = false) String version) {
        return ResponseEntity.ok(service.updateDocument(id, file, version));
    }


    @PostMapping("/requests/{requestId}")
    public ResponseEntity<?> uploadRequestDocument(@PathVariable String requestId,
                                                   @RequestParam("file") MultipartFile file) {
        return ResponseEntity.ok(service.storeDocument(file, null, requestId, null));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or hasRole('COORDINATOR')")
    public ResponseEntity<?> deleteDocument(@PathVariable Long id) {
        service.deleteDocument(id);
        return ResponseEntity.ok().build();
    }
}
