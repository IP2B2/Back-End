package com.UAIC.ISMA.service;

import com.UAIC.ISMA.entity.Notification;
import com.UAIC.ISMA.entity.RequestApproval;
import com.UAIC.ISMA.entity.enums.ApprovalStatus;
import com.UAIC.ISMA.entity.enums.RoleName;
import com.UAIC.ISMA.dto.RequestApprovalDTO;
import com.UAIC.ISMA.exception.RequestApprovalNotFoundException;
import com.UAIC.ISMA.exception.UnauthorizedException;
import com.UAIC.ISMA.exception.UserNotFoundException;
import com.UAIC.ISMA.mapper.RequestApprovalMapper;
import com.UAIC.ISMA.repository.AccessRequestRepository;
import com.UAIC.ISMA.repository.NotificationRepository;
import com.UAIC.ISMA.repository.RequestApprovalRepository;
import com.UAIC.ISMA.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestApprovalService {

    public final RequestApprovalRepository requestApprovalRepository;
    public final UserRepository userRepository;
    public final AccessRequestRepository accessRequestRepository;
    @Autowired
    NotificationRepository notificationRepository;

    @Autowired
    public RequestApprovalService(RequestApprovalRepository requestApprovalRepository,
                                  AccessRequestRepository accessRequestRepository,
                                  UserRepository userRepository) {
        this.requestApprovalRepository = requestApprovalRepository;
        this.accessRequestRepository = accessRequestRepository;
        this.userRepository = userRepository;
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
        if (dto.getAccessRequestId() != null) {
            var accessRequest = accessRequestRepository.findById(dto.getAccessRequestId())
                    .orElseThrow(() -> new RequestApprovalNotFoundException(dto.getAccessRequestId()));
            entity.setAccessRequest(accessRequest);
        }
        if (dto.getApproverId() != null) {
            var approver = userRepository.findById(dto.getApproverId())
                    .orElseThrow(() -> new UserNotFoundException(dto.getApproverId()));
            entity.setApprover(approver);
        }
        entity.setApprovalStatus(ApprovalStatus.PENDING);
        RequestApproval savedRequestApproval = requestApprovalRepository.save(entity);
        return RequestApprovalMapper.toDTO(savedRequestApproval);
    }

    public RequestApprovalDTO update(Long id, RequestApprovalDTO dto) {
        String username = ((UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUsername();

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException(username));
        var role = user.getRole();

        if (role.getRoleName() != RoleName.ADMIN && role.getRoleName() != RoleName.COORDONATOR) {
            throw new UnauthorizedException("Only ADMIN or COORDONATOR can update approval.");
        }

        RequestApproval existing = requestApprovalRepository.findById(id)
                .orElseThrow(() -> new RequestApprovalNotFoundException(id));

        existing.setApprovalStatus(ApprovalStatus.valueOf(dto.getApprovalStatus()));
        existing.setApprovalDate(LocalDateTime.now());
        existing.setComments(dto.getComments());
        existing.setApprover(user);

        RequestApproval updatedRequestApproval = requestApprovalRepository.save(existing);

        var accessRequest = updatedRequestApproval.getAccessRequest();
        if (accessRequest != null && accessRequest.getUser() != null) {
            String message = "Your access request has been updated. Approval status: " + updatedRequestApproval.getApprovalStatus();
            Notification notification = new Notification(message, accessRequest.getUser());
            notificationRepository.save(notification);
        }

        return RequestApprovalMapper.toDTO(updatedRequestApproval);
    }

    public void delete(Long id) {
        RequestApproval requestApproval = requestApprovalRepository.findById(id)
                .orElseThrow(() -> new RequestApprovalNotFoundException(id));
        requestApprovalRepository.delete(requestApproval);
    }

}
