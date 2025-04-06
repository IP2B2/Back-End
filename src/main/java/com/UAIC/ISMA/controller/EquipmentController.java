package com.UAIC.ISMA.controller;


import com.UAIC.ISMA.dao.Equipment;
import com.UAIC.ISMA.dto.EquipmentDTO;
import com.UAIC.ISMA.service.EquipmentService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping
    public List<EquipmentDTO> getAllEquipments() {
        return equipmentService.getAllEquipments();
    }

    @GetMapping("/{id}")
    public EquipmentDTO getEquipmentById(@PathVariable Long id) {
        return equipmentService.getEquipmentById(id)
                .orElseThrow(() -> new RuntimeException("No equipment found with id " + id));
    }

    @PostMapping
    public EquipmentDTO createEquipment(@RequestBody EquipmentDTO equipmentDTO) {
        return equipmentService.createEquipment(equipmentDTO);
    }

    @PutMapping("/{id}")
    public EquipmentDTO updateEquipment(@PathVariable Long id, @RequestBody EquipmentDTO equipmentDTO) {
        return equipmentService.updateEquipment(equipmentDTO, id);
    }

    @PatchMapping("/{id}")
    public EquipmentDTO patchEquipment(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return equipmentService.partialUpdateEquipment(id, updates);
    }

    @DeleteMapping("/{id}")
    public void deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
    }
}
