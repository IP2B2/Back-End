package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dao.Laboratory;
import com.UAIC.ISMA.dto.LaboratoryDTO;
import com.UAIC.ISMA.repository.LaboratoryRepository;
import com.UAIC.ISMA.service.LaboratoryService;
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
    public List<LaboratoryDTO> getLaboratories() {
        return laboratoryService.getAlLaboratories();
    }

    @GetMapping("/{id}")
    public LaboratoryDTO getLaboratory(@PathVariable Long id) {
        return laboratoryService.getLaboratoryById(id).orElseThrow(() -> new RuntimeException("Laboratory not found"));
    }

    @PostMapping
    public LaboratoryDTO createLaboratory(@RequestBody LaboratoryDTO laboratoryDTO) {
        return laboratoryService.createLaboratory(laboratoryDTO);
    }

    @PutMapping("/{id}")
    public LaboratoryDTO updateLaboratory(@PathVariable Long id, @RequestBody LaboratoryDTO laboratoryDTO) {
        return laboratoryService.updateLaboratory(id, laboratoryDTO);
    }

    @PatchMapping("/{id}")
    public LaboratoryDTO patchLaboratory(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return laboratoryService.partialUpdateLaboratory(id, updates);
    }

    @DeleteMapping("/{id}")
    public void deleteLaboratory(@PathVariable Long id) {
        laboratoryService.deleteLaboratory(id);
    }
}
