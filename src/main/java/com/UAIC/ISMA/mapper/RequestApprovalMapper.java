package com.UAIC.ISMA.mapper;

import com.UAIC.ISMA.dao.RequestApproval;
import com.UAIC.ISMA.dao.enums.ApprovalStatus;
import com.UAIC.ISMA.dto.RequestApprovalDTO;

public class RequestApprovalMapper {

    public static RequestApprovalDTO toDTO(RequestApproval entity) {
        return new RequestApprovalDTO(
                entity.getId(),
                entity.getApprovalStatus().toString(),
                entity.getApprovalDate(),
                entity.getComments()
        );
    }

    public static RequestApproval toEntity(RequestApprovalDTO dto) {
        RequestApproval entity = new RequestApproval();
        entity.setApprovalStatus(ApprovalStatus.valueOf(dto.getApprovalStatus()));
        entity.setApprovalDate(dto.getApprovalDate());
        entity.setComments(dto.getComments());
        return entity;
    }
}
