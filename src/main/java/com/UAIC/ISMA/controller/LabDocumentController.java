package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.LabDocumentDTO;
import com.UAIC.ISMA.service.LabDocumentService;
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/labdocuments")
public class LabDocumentController {

    private static final Logger logger = LoggerFactory.getLogger(LabDocumentController.class);
    private final LabDocumentService service;

    public LabDocumentController(LabDocumentService service) {
        this.service = service;
    }

    @Operation(summary = "Listare documente laborator", description = "Returnează toate documentele active (nearchivate) pentru laboratorul specificat")
    @GetMapping("/labs/{labId}")
    public ResponseEntity<List<LabDocumentDTO>> getDocumentsByLab(@PathVariable String labId) {
        logger.info("GET /labdocuments/labs/{} - listare documente", labId);
        return ResponseEntity.ok(service.getDocumentsByLab(labId));
    }

    @Operation(summary = "Descărcare document", description = "Descarcă documentul cu ID-ul specificat")
    @GetMapping("/{id}/download")
    public ResponseEntity<Resource> downloadDocument(@PathVariable Long id) {
        logger.info("GET /labdocuments/{}/download - descărcare document", id);
        return service.downloadDocument(id);
    }

    @Operation(summary = "Upload document oficial laborator", description = "Permite unui administrator sau coordonator să încarce un document oficial în laboratorul specificat")
    @PostMapping("/labs/{labId}")

    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORDONATOR')")

    public ResponseEntity<LabDocumentDTO> uploadLabDocument(@PathVariable String labId,
                                                            @RequestParam("file") MultipartFile file,
                                                            @RequestParam(required = false) String version) {
        logger.info("POST /labdocuments/labs/{} - upload fișier: {}", labId, file.getOriginalFilename());
        return ResponseEntity.ok(service.storeDocument(file, labId, null, version));
    }

    @Operation(summary = "Upload document pentru cerere", description = "Permite unui utilizator să atașeze un document la o cerere de acces")
    @PostMapping("/requests/{requestId}")
    public ResponseEntity<LabDocumentDTO> uploadRequestDocument(@PathVariable String requestId,
                                                                @RequestParam("file") MultipartFile file) {
        logger.info("POST /labdocuments/requests/{} - upload fișier cerere: {}", requestId, file.getOriginalFilename());
        return ResponseEntity.ok(service.storeDocument(file, null, requestId, null));
    }

    @Operation(summary = "Șterge document", description = "Permite unui administrator sau coordonator să șteargă un document după ID")
    @DeleteMapping("/{id}")

    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORDONATOR')")

    public ResponseEntity<Void> deleteDocument(@PathVariable Long id) {
        logger.info("DELETE /labdocuments/{} - ștergere document", id);
        service.deleteDocument(id);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Actualizare document", description = "Înlocuiește documentul cu un fișier nou și marchează versiunea veche ca arhivată")
    @PutMapping("/{id}")

    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORDONATOR')")

    public ResponseEntity<LabDocumentDTO> updateDocument(@PathVariable Long id,
                                                         @RequestParam("file") MultipartFile file,
                                                         @RequestParam(required = false) String version) {
        logger.info("PUT /labdocuments/{} - actualizare document cu: {}", id, file.getOriginalFilename());
        return ResponseEntity.ok(service.updateDocument(id, file, version));
    }
}
