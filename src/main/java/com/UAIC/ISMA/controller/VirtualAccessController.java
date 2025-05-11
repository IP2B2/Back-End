package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.VirtualAccessDTO;
import com.UAIC.ISMA.service.VirtualAccessService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/virtual-access")
@Tag(name = "VirtualAccess", description = "Operations related to virtual access")
public class VirtualAccessController {

    private final VirtualAccessService virtualAccessService;

    @Autowired
    public VirtualAccessController(VirtualAccessService virtualAccessService) {
        this.virtualAccessService = virtualAccessService;
    }

    @GetMapping("/all")
    @Operation(
            summary = "Get all virtual accesses",
            description = "Returns a list with all the virtual accesses in the database"
    )
    public ResponseEntity<List<VirtualAccessDTO>> getAllVirtualAccess() {
        List<VirtualAccessDTO> accesses = virtualAccessService.findAll();
        return ResponseEntity.ok(accesses);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get virtual access by id",
            description = "Returns a single virtual access by its unique id"
    )
    public ResponseEntity<VirtualAccessDTO> getVirtualAccessById(
            @Parameter(description = "VirtualAccess id")
            @PathVariable Long id) {
        VirtualAccessDTO virtualAccessDTO = virtualAccessService.findById(id);
        return ResponseEntity.ok(virtualAccessDTO);
    }

    @PostMapping
    @Operation(
            summary = "Create a new virtual access",
            description = "Creates a new virtual access with the specified details"
    )
    public ResponseEntity<VirtualAccessDTO> createVirtualAccess(
            @Parameter(description = "Virtual access data to create")
            @RequestBody VirtualAccessDTO virtualAccessDTO) {
        VirtualAccessDTO created = virtualAccessService.create(virtualAccessDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update a virtual access",
            description = "Updates a virtual access with the specified id with the specified details"
    )
    public ResponseEntity<VirtualAccessDTO> updateVirtualAccess(
            @Parameter(description = "VirtualAccess id")
            @PathVariable Long id,
            @Parameter(description = "Virtual access data to update")
            @RequestBody VirtualAccessDTO virtualAccessDTO) {
        VirtualAccessDTO updated = virtualAccessService.update(id, virtualAccessDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a virtual access",
            description = "Deletes a virtual access with the specified id"
    )
    public ResponseEntity<Void> deleteVirtualAccess(
            @Parameter(description = "Virtual access id")
            @PathVariable Long id) {
        virtualAccessService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
