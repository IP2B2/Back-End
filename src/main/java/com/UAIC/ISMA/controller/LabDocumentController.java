package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.LabDocumentDTO;
import com.UAIC.ISMA.exception.EntityNotFoundException;
import com.UAIC.ISMA.service.LabDocumentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/labDocs")
public class LabDocumentController {
    private final LabDocumentService labDocumentService;

    public LabDocumentController(LabDocumentService labDocumentService) {
        this.labDocumentService = labDocumentService;
    }

    @GetMapping
    public ResponseEntity<List<LabDocumentDTO>> getAllLabDocs() {
        List<LabDocumentDTO> labDocumentList = labDocumentService.getAllDocuments();
        return ResponseEntity.ok(labDocumentList);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LabDocumentDTO> getDocById(@PathVariable Long id) {
        LabDocumentDTO get = labDocumentService.getDocumentsById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nu exista Documente pentru Laboratorul cu Id-ul: " + id));
        return ResponseEntity.ok(get);
    }

    @GetMapping("/laboratory/{laboratoryId}")
    public ResponseEntity<List<LabDocumentDTO>>getDocsByLabId(@PathVariable("laboratoryId") Long laboratoryId){
        List<LabDocumentDTO> labDocumentDTOList = labDocumentService.getDocumentsByLabId(laboratoryId);
        return ResponseEntity.ok(labDocumentDTOList);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN') or hasRole('Coordinator')")
    public LabDocumentDTO createLabDocument(@RequestBody LabDocumentDTO labDocumentDTO) {
        return labDocumentService.createDocument(labDocumentDTO);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LabDocumentDTO> updateLabDoc(@PathVariable Long id, @RequestBody LabDocumentDTO labDocumentDTO) {
        LabDocumentDTO update = labDocumentService.updateLabDocument(id, labDocumentDTO);
        return ResponseEntity.ok(update);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<LabDocumentDTO> deleteLabDoc(@PathVariable Long id) {
        labDocumentService.deleteLabDocument(id);
        return ResponseEntity.noContent().build();
    }
}
