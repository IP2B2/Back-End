package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dao.Laboratory;
import com.UAIC.ISMA.dto.LaboratoryDTO;
import com.UAIC.ISMA.exception.EntityNotFoundException;
import com.UAIC.ISMA.repository.LaboratoryRepository;
import com.UAIC.ISMA.service.LaboratoryService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/laboratories")
public class LaboratoryController {

    private final LaboratoryService laboratoryService;

    public LaboratoryController(LaboratoryService laboratoryService) {
        this.laboratoryService = laboratoryService;
    }

    @GetMapping
    public ResponseEntity<List<LaboratoryDTO>> getAllLaboratories() {
        List<LaboratoryDTO> labs = laboratoryService.getAlLaboratories();
        return ResponseEntity.ok(labs);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LaboratoryDTO> getLaboratoryById(@PathVariable long id) {
        LaboratoryDTO lab = laboratoryService.getLaboratoryById(id)
                .orElseThrow(() -> new EntityNotFoundException("Laboratory not found with id: " + id));
        return ResponseEntity.ok(lab);
    }

    @PostMapping
    public ResponseEntity<LaboratoryDTO> createLaboratory(@RequestBody LaboratoryDTO laboratoryDTO) {
        LaboratoryDTO created = laboratoryService.createLaboratory(laboratoryDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LaboratoryDTO> updateLaboratory(@PathVariable Long id, @RequestBody LaboratoryDTO laboratoryDTO) {
        LaboratoryDTO updated = laboratoryService.updateLaboratory(id, laboratoryDTO);
        return ResponseEntity.ok(updated);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<LaboratoryDTO> patchLaboratory(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        LaboratoryDTO updated = laboratoryService.partialUpdateLaboratory(id, updates);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLaboratory(@PathVariable Long id) {
        laboratoryService.deleteLaboratory(id);
        return ResponseEntity.noContent().build();
    }
}
