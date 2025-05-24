package com.UAIC.ISMA.service;

import com.UAIC.ISMA.entity.AccessRequest;
import com.UAIC.ISMA.entity.Notification;
import com.UAIC.ISMA.entity.RequestApproval;
import com.UAIC.ISMA.entity.enums.ApprovalStatus;
import com.UAIC.ISMA.dto.RequestApprovalDTO;
import com.UAIC.ISMA.exception.AccessRequestNotFoundException;
import com.UAIC.ISMA.exception.InvalidInputException;
import com.UAIC.ISMA.exception.RequestApprovalNotFoundException;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RequestApprovalService {

    private static final Logger logger = LogManager.getLogger(RequestApprovalService.class);

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

    public List<RequestApprovalDTO> findAll(Long accessRequestId, Long approverId) {
        logger.info("Fetching all request approvals. ApproverId: {}, AccessRequestId: {}", approverId, accessRequestId);
        List<RequestApproval> approvals;
        if (approverId != null && accessRequestId != null) {
            approvals = requestApprovalRepository.findByAccessRequest_IdAndApprover_Id(accessRequestId, approverId);
        } else if (approverId != null) {
            approvals = requestApprovalRepository.findByApprover_Id(approverId);
        } else if (accessRequestId != null) {
            approvals = requestApprovalRepository.findByAccessRequest_Id(accessRequestId);
        } else {
            approvals = requestApprovalRepository.findAll();
        }
        return approvals.stream()
                .map(RequestApprovalMapper::toDTO)
                .collect(Collectors.toList());
    }

    public RequestApprovalDTO findById(Long id) {
        logger.info("Fetching request approval with id: {}", id);
        RequestApproval requestApproval = requestApprovalRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Request approval with id {} not found", id);
                    return new RequestApprovalNotFoundException(id);
                });
        return RequestApprovalMapper.toDTO(requestApproval);
    }

    public RequestApprovalDTO create(RequestApprovalDTO dto) {
        if (dto.getAccessRequestId() == null) {
            logger.warn("Access request ID must be provided in the request body.");
            throw new InvalidInputException("Access request ID must be provided.");
        }
        logger.info("Creating new request approval for accessRequestId: {}", dto.getAccessRequestId());
        RequestApproval entity = RequestApprovalMapper.toEntity(dto);
        AccessRequest accessRequest = accessRequestRepository.findById(dto.getAccessRequestId())
                .orElseThrow(() -> {
                    logger.warn("Access request with id {} not found", dto.getAccessRequestId());
                    return new AccessRequestNotFoundException(dto.getAccessRequestId());
                });
        entity.setAccessRequest(accessRequest);
        entity.setApprover(null);
        RequestApproval savedRequestApproval = requestApprovalRepository.save(entity);
        logger.info("Request approval created with id: {}", savedRequestApproval.getId());
        return RequestApprovalMapper.toDTO(savedRequestApproval);
    }

    public RequestApprovalDTO update(Long id, RequestApprovalDTO dto) {
        if (dto.getApprovalStatus() == null) {
            logger.warn("Approval status must be provided in the request body.");
            throw new InvalidInputException("Approval status must be provided.");
        }
        logger.info("Updating request approval with id: {}", id);
        String username = ((UserDetails) SecurityContextHolder.getContext()
                .getAuthentication().getPrincipal()).getUsername();

        var user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    logger.warn("User with name {} not found", username);
                    return new UserNotFoundException(username);
                });

        RequestApproval existing = requestApprovalRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Request approval with id {} not found", id);
                    return new RequestApprovalNotFoundException(id);
                });

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
            logger.info("Notification sent to user with id: {}", accessRequest.getUser().getId());
        }

        return RequestApprovalMapper.toDTO(updatedRequestApproval);
    }

    public void delete(Long id) {
        logger.info("Deleting request approval with id: {}", id);
        RequestApproval requestApproval = requestApprovalRepository.findById(id)
                .orElseThrow(() -> {
                    logger.warn("Request approval with id {} not found", id);
                    return new RequestApprovalNotFoundException(id);
                });
        requestApprovalRepository.delete(requestApproval);
    }

}
