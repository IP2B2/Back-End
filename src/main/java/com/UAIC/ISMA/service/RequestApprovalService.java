package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dao.AccessRequest;
import com.UAIC.ISMA.dao.RequestApproval;
import com.UAIC.ISMA.dao.User;
import com.UAIC.ISMA.dao.enums.ApprovalStatus;
import com.UAIC.ISMA.dto.RequestApprovalDTO;
import com.UAIC.ISMA.exception.EntityNotFoundException;
import com.UAIC.ISMA.exception.InvalidInputException;
import com.UAIC.ISMA.repository.AccessRequestRepository;
import com.UAIC.ISMA.repository.RequestApprovalRepository;
import com.UAIC.ISMA.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RequestApprovalService {

    public final RequestApprovalRepository requestApprovalRepository;
    public final AccessRequestRepository accessRequestRepository;
    public final UserRepository userRepository;

    @Autowired
    public RequestApprovalService(RequestApprovalRepository requestApprovalRepository, AccessRequestRepository accessRequestRepository, UserRepository userRepository) {
        this.requestApprovalRepository = requestApprovalRepository;
        this.accessRequestRepository = accessRequestRepository;
        this.userRepository = userRepository;
    }

    public List<RequestApprovalDTO> getAllRequestApprovals() {
        return requestApprovalRepository.findAll().stream()
                .map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<RequestApprovalDTO> getRequestApprovalById(Long id) {
        return requestApprovalRepository.findById(id).map(this::convertToDTO);
    }

    public RequestApprovalDTO createRequestApproval(RequestApprovalDTO requestApprovalDTO) {
        RequestApproval requestApproval = convertToEntity(requestApprovalDTO);
        RequestApproval savedRequestApproval = requestApprovalRepository.save(requestApproval);
        return convertToDTO(savedRequestApproval);
    }

    public RequestApprovalDTO updateRequestApproval(Long id, RequestApprovalDTO requestApprovalDTO) {
        RequestApproval requestApproval = convertToEntity(requestApprovalDTO);
        requestApproval.setId(id);
        return convertToDTO(requestApprovalRepository.save(requestApproval));
    }

    public void deleteRequestApproval(Long id) {
        requestApprovalRepository.deleteById(id);
    }

    public RequestApprovalDTO partialUpdateRequestApproval(Long id, Map<String, Object> updates) {
        RequestApproval requestApproval = requestApprovalRepository.findById(id).orElseThrow(()
                -> new EntityNotFoundException("RequestApproval with id " + id + " not found"));

        updates.forEach((key, value) -> {
            switch (key) {
                case "approvalStatus" -> requestApproval.setApprovalStatus((ApprovalStatus) value);
                case "approvalDate" -> requestApproval.setApprovalDate((LocalDateTime) value);
                case "comments" -> requestApproval.setComments((String) value);
                default -> throw new InvalidInputException("Invalid key " + key);
            }
        });

        RequestApproval updated = requestApprovalRepository.save(requestApproval);
        return convertToDTO(updated);
    }

    private RequestApprovalDTO convertToDTO(RequestApproval requestApproval) {
        RequestApprovalDTO dto = new RequestApprovalDTO();
        dto.setId(requestApproval.getId());
        dto.setApprovalStatus(requestApproval.getApprovalStatus().name());
        dto.setApprovalDate(requestApproval.getApprovalDate());
        dto.setComments(requestApproval.getComments());
        if (requestApproval.getAccessRequest() != null) {
            dto.setAccessRequestId(requestApproval.getAccessRequest().getId());
        }
        if (requestApproval.getApprover() != null) {
            dto.setApproverId(requestApproval.getApprover().getId());
        }
        return dto;
    }

    private RequestApproval convertToEntity(RequestApprovalDTO dto) {
        RequestApproval requestApproval = new RequestApproval();
        requestApproval.setApprovalStatus(ApprovalStatus.valueOf(dto.getApprovalStatus()));
        requestApproval.setApprovalDate(dto.getApprovalDate());
        requestApproval.setComments(dto.getComments());
        if (dto.getAccessRequestId() != null) {
            AccessRequest accessRequest = accessRequestRepository.findById(dto.getAccessRequestId())
                    .orElseThrow(() -> new EntityNotFoundException("AccessRequest with id " + dto.getAccessRequestId() + " not found"));
            requestApproval.setAccessRequest(accessRequest);
        }
        if (dto.getApproverId() != null) {
            User approver = userRepository.findById(dto.getApproverId())
                    .orElseThrow(() -> new EntityNotFoundException("User with id " + dto.getApproverId() + " not found"));
            requestApproval.setApprover(approver);
        }
        return requestApproval;
    }

}
