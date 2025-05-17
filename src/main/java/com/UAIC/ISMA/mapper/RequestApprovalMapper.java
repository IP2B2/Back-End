package com.UAIC.ISMA.mapper;

import com.UAIC.ISMA.entity.RequestApproval;
import com.UAIC.ISMA.entity.enums.ApprovalStatus;
import com.UAIC.ISMA.dto.RequestApprovalDTO;

import java.time.LocalDateTime;

public class RequestApprovalMapper {

    public static RequestApprovalDTO toDTO(RequestApproval entity) {
        return new RequestApprovalDTO(
                entity.getId(),
                entity.getApprovalStatus() != null ? entity.getApprovalStatus().toString() : null,
                entity.getApprovalDate(),
                entity.getComments(),
                entity.getApprover() != null ? entity.getApprover().getId() : null,
                entity.getAccessRequest() != null ? entity.getAccessRequest().getId() : null
        );
    }

    public static RequestApproval toEntity(RequestApprovalDTO dto) {
        RequestApproval entity = new RequestApproval();
        if (dto.getApprovalStatus() != null) {
            entity.setApprovalStatus(ApprovalStatus.valueOf(dto.getApprovalStatus()));
            entity.setApprovalDate(LocalDateTime.now());
        } else {
            entity.setApprovalStatus(null);
        }
        entity.setComments(dto.getComments());
        return entity;
    }
}
