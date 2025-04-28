package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.VirtualAccessDTO;
import com.UAIC.ISMA.exception.EntityNotFoundException;
import com.UAIC.ISMA.service.VirtualAccessService;
import org.springframework.beans.factory.annotation.Autowired;
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
    public List<VirtualAccessDTO> getAllVirtualAccesses() {
        return virtualAccessService.findAll();
    }

    @GetMapping("/{id}")
    public VirtualAccessDTO getVirtualAccessById(@PathVariable Long id) {
        return virtualAccessService.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("VirtualAccess not found with id " + id));
    }

    @PostMapping
    public VirtualAccessDTO createVirtualAccess(@RequestBody VirtualAccessDTO virtualAccessDTO) {
        return virtualAccessService.save(virtualAccessDTO);
    }

    @PutMapping("/{id}")
    public VirtualAccessDTO updateVirtualAccess(@PathVariable Long id, @RequestBody VirtualAccessDTO virtualAccessDTO) {
        return virtualAccessService.update(id, virtualAccessDTO);
    }

    @DeleteMapping("/{id}")
    public void deleteVirtualAccess(@PathVariable Long id) {
        virtualAccessService.deleteById(id);
    }
}
