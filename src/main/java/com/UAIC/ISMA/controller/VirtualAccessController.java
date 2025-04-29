package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.VirtualAccessDTO;
import com.UAIC.ISMA.exception.EntityNotFoundException;
import com.UAIC.ISMA.service.VirtualAccessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/virtual-access")
public class VirtualAccessController {

    private final VirtualAccessService virtualAccessService;

    @Autowired
    public VirtualAccessController(VirtualAccessService virtualAccessService) {
        this.virtualAccessService = virtualAccessService;
    }

    @GetMapping
    public ResponseEntity<List<VirtualAccessDTO>> getAllVirtualAccesses() {
        List<VirtualAccessDTO> accesses = virtualAccessService.findAll();
        return ResponseEntity.ok(accesses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<VirtualAccessDTO> getVirtualAccessById(@PathVariable Long id) {
        return virtualAccessService.findById(id)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new EntityNotFoundException("VirtualAccess not found with id " + id));
    }

    @PostMapping
    public ResponseEntity<VirtualAccessDTO> createVirtualAccess(@RequestBody VirtualAccessDTO virtualAccessDTO) {
        VirtualAccessDTO created = virtualAccessService.save(virtualAccessDTO);
        return ResponseEntity.ok(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<VirtualAccessDTO> updateVirtualAccess(@PathVariable Long id, @RequestBody VirtualAccessDTO virtualAccessDTO) {
        VirtualAccessDTO updated = virtualAccessService.update(id, virtualAccessDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteVirtualAccess(@PathVariable Long id) {
        virtualAccessService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
