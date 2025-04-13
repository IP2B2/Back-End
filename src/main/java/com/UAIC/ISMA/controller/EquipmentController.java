package com.UAIC.ISMA.controller;


import com.UAIC.ISMA.dto.EquipmentDTO;
import com.UAIC.ISMA.exception.EntityNotFoundException;
import com.UAIC.ISMA.service.EquipmentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipment")
public class EquipmentController {

    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @GetMapping
    public ResponseEntity<List<EquipmentDTO>> getAllEquipment() {
        List<EquipmentDTO> equipments = equipmentService.getAllEquipments();
        return ResponseEntity.ok(equipments);
    }

    @GetMapping("/{id}")
    public ResponseEntity<EquipmentDTO> getEquipmentById(@PathVariable Long id) {
        EquipmentDTO equipment = equipmentService.getEquipmentById(id)
                .orElseThrow(() -> new EntityNotFoundException("No equipment found with id: " + id));
        return ResponseEntity.ok(equipment);
    }

    @PostMapping
    public ResponseEntity<EquipmentDTO> createEquipment(@RequestBody EquipmentDTO equipmentDTO) {
        EquipmentDTO created = equipmentService.createEquipment(equipmentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<EquipmentDTO> updateEquipment(@PathVariable Long id, @RequestBody EquipmentDTO equipmentDTO) {
        EquipmentDTO updated = equipmentService.updateEquipment(equipmentDTO, id);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteEquipment(@PathVariable Long id) {
        equipmentService.deleteEquipment(id);
        return ResponseEntity.noContent().build();
    }
}
