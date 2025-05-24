package com.UAIC.ISMA.mapper;

import com.UAIC.ISMA.entity.RequestApproval;
import com.UAIC.ISMA.dto.RequestApprovalDTO;

public class RequestApprovalMapper {

    public static RequestApprovalDTO toDTO(RequestApproval entity) {
        return new RequestApprovalDTO(
                entity.getId(),
                entity.getApprovalStatus() != null ? entity.getApprovalStatus().toString() : null,
                entity.getApprovalDate(),
                entity.getComments(),
                entity.getAccessRequest() != null ? entity.getAccessRequest().getId() : null,
                entity.getApprover() != null ? entity.getApprover().getId() : null
        );
    }

    public static RequestApproval toEntity(RequestApprovalDTO dto) {
        RequestApproval entity = new RequestApproval();
        entity.setComments(dto.getComments());
        return entity;
    }
}
