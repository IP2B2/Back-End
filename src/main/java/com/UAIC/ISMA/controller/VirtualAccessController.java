package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.VirtualAccessDTO;
import com.UAIC.ISMA.service.VirtualAccessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/virtual-access")
@Tag(name = "VirtualAccess", description = "Operations related to virtual access")
@Slf4j
public class VirtualAccessController {

    private final VirtualAccessService virtualAccessService;

    @Autowired
    public VirtualAccessController(VirtualAccessService virtualAccessService) {
        this.virtualAccessService = virtualAccessService;
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORDONATOR')")
    @GetMapping
    @Operation(summary = "Get all virtual accesses", description = "Returns a list with all the virtual accesses in the database")
    public ResponseEntity<List<VirtualAccessDTO>> getAllVirtualAccess() {
        log.info("GET /virtual-access - Fetching all entries");
        List<VirtualAccessDTO> accesses = virtualAccessService.findAll();
        return ResponseEntity.ok(accesses);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORDONATOR')")
    @GetMapping("/{id}")
    @Operation(summary = "Get virtual access by id", description = "Returns a single virtual access by its unique id")
    public ResponseEntity<VirtualAccessDTO> getVirtualAccessById(@PathVariable Long id) {
        log.info("GET /virtual-access/{} - Fetching by ID", id);
        VirtualAccessDTO virtualAccessDTO = virtualAccessService.findById(id);
        return ResponseEntity.ok(virtualAccessDTO);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORDONATOR')")
    @PostMapping
    @Operation(summary = "Create a new virtual access", description = "Creates a new virtual access with the specified details")
    public ResponseEntity<VirtualAccessDTO> createVirtualAccess(@RequestBody VirtualAccessDTO virtualAccessDTO) {
        log.info("POST /virtual-access - Creating new access for request ID {}", virtualAccessDTO.getAccessRequestId());
        VirtualAccessDTO created = virtualAccessService.create(virtualAccessDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORDONATOR')")
    @PutMapping("/{id}")
    @Operation(summary = "Update a virtual access", description = "Updates a virtual access with the specified id with the specified details")
    public ResponseEntity<VirtualAccessDTO> updateVirtualAccess(@PathVariable Long id, @RequestBody VirtualAccessDTO virtualAccessDTO) {
        log.info("PUT /virtual-access/{} - Updating access", id);
        VirtualAccessDTO updated = virtualAccessService.update(id, virtualAccessDTO);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'COORDONATOR')")
    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a virtual access", description = "Deletes a virtual access with the specified id")
    public ResponseEntity<Void> deleteVirtualAccess(@PathVariable Long id) {
        log.info("DELETE /virtual-access/{} - Deleting access", id);
        virtualAccessService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
