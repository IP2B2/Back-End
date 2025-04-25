package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.RequestApprovalDTO;
import com.UAIC.ISMA.exception.EntityNotFoundException;
import com.UAIC.ISMA.service.RequestApprovalService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/request-approvals")
public class RequestApprovalController {

    private final RequestApprovalService requestApprovalService;

    public RequestApprovalController(RequestApprovalService requestApprovalService) {
        this.requestApprovalService = requestApprovalService;
    }

    @GetMapping("/all")
    public ResponseEntity<List<RequestApprovalDTO>> getAllRequestApprovals() {
        List<RequestApprovalDTO> requestApprovals = requestApprovalService.getAllRequestApprovals();
        return ResponseEntity.ok(requestApprovals);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RequestApprovalDTO> getRequestApprovalById(@PathVariable Long id) {
        RequestApprovalDTO requestApproval = requestApprovalService.getRequestApprovalById(id)
                .orElseThrow(() -> new EntityNotFoundException("Request approval not found with id: " + id));
        return ResponseEntity.ok(requestApproval);
    }

    @PostMapping
    public ResponseEntity<RequestApprovalDTO> createRequestApproval(@RequestBody RequestApprovalDTO requestApprovalDTO) {
        RequestApprovalDTO created = requestApprovalService.createRequestApproval(requestApprovalDTO);
        return ResponseEntity.status(HttpStatus.OK).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<RequestApprovalDTO> updateRequestApproval(@PathVariable Long id, @RequestBody RequestApprovalDTO requestApprovalDTO) {
        RequestApprovalDTO updated = requestApprovalService.updateRequestApproval(id, requestApprovalDTO);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRequestApproval(@PathVariable Long id) {
        requestApprovalService.deleteRequestApproval(id);
        return ResponseEntity.noContent().build();
    }
}
