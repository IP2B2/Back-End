package com.UAIC.ISMA.service;

import com.UAIC.ISMA.entity.RequestApproval;
import com.UAIC.ISMA.entity.enums.ApprovalStatus;
import com.UAIC.ISMA.dto.RequestApprovalDTO;
import com.UAIC.ISMA.exception.RequestApprovalNotFoundException;
import com.UAIC.ISMA.mapper.RequestApprovalMapper;
import com.UAIC.ISMA.repository.AccessRequestRepository;
import com.UAIC.ISMA.repository.RequestApprovalRepository;
import com.UAIC.ISMA.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestApprovalService {

    public final RequestApprovalRepository requestApprovalRepository;

    @Autowired
    public RequestApprovalService(RequestApprovalRepository requestApprovalRepository,
                                  AccessRequestRepository accessRequestRepository,
                                  UserRepository userRepository) {
        this.requestApprovalRepository = requestApprovalRepository;
    }

    public List<RequestApprovalDTO> findAll(Long approverId, Long accessRequestId) {
        return requestApprovalRepository.findAll()
                .stream()
                .map(RequestApprovalMapper::toDTO)
                .collect(Collectors.toList());
    }

    public RequestApprovalDTO findById(Long id) {
        RequestApproval requestApproval = requestApprovalRepository.findById(id)
                .orElseThrow(() -> new RequestApprovalNotFoundException(id));
        return RequestApprovalMapper.toDTO(requestApproval);
    }

    public RequestApprovalDTO create(RequestApprovalDTO dto) {
        RequestApproval entity = RequestApprovalMapper.toEntity(dto);
        RequestApproval savedRequestApproval = requestApprovalRepository.save(entity);
        return RequestApprovalMapper.toDTO(savedRequestApproval);
    }

    public RequestApprovalDTO update(Long id, RequestApprovalDTO dto) {
        RequestApproval existing = requestApprovalRepository.findById(id)
                .orElseThrow(() -> new RequestApprovalNotFoundException(id));

        existing.setApprovalStatus(ApprovalStatus.valueOf(dto.getApprovalStatus()));
        existing.setApprovalDate(dto.getApprovalDate());
        existing.setComments(dto.getComments());

        RequestApproval updatedRequestApproval = requestApprovalRepository.save(existing);
        return RequestApprovalMapper.toDTO(updatedRequestApproval);
    }

    public void delete(Long id) {
        RequestApproval requestApproval = requestApprovalRepository.findById(id)
                .orElseThrow(() -> new RequestApprovalNotFoundException(id));
        requestApprovalRepository.delete(requestApproval);
    }

}
