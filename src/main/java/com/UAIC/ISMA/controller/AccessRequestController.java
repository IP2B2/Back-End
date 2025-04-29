package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.AccessRequestDTO;
import com.UAIC.ISMA.service.AccessRequestService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/access-requests")
public class AccessRequestController {

    private final AccessRequestService accessRequestService;

    public AccessRequestController(AccessRequestService accessRequestService) {
        this.accessRequestService = accessRequestService;
    }

    @GetMapping
    public ResponseEntity<List<AccessRequestDTO>> getAllAccessRequests() {
        return ResponseEntity.ok(accessRequestService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccessRequestDTO> getAccessRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(accessRequestService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AccessRequestDTO> createAccessRequest(@RequestBody AccessRequestDTO dto) {
        return ResponseEntity.ok(accessRequestService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AccessRequestDTO> updateAccessRequest(@PathVariable Long id, @RequestBody AccessRequestDTO dto) {
        return ResponseEntity.ok(accessRequestService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccessRequest(@PathVariable Long id) {
        accessRequestService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
