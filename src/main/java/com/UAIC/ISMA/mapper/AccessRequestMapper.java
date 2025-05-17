package com.UAIC.ISMA.mapper;

import com.UAIC.ISMA.entity.AccessRequest;
import com.UAIC.ISMA.dto.AccessRequestDTO;

public class AccessRequestMapper {

    public static AccessRequestDTO toDTO(AccessRequest entity) {
        return new AccessRequestDTO(
                entity.getId(),
                entity.getRequestDate(),
                entity.getStatus(),
                entity.getRequestType(),
                entity.getProposalFile(),
                entity.getExpectedReturnDate(),
                entity.getUser().getId(),
                entity.getEquipment().getId()
        );
    }

    public static AccessRequest toEntity(AccessRequestDTO dto) {
        AccessRequest entity = new AccessRequest();
        entity.setRequestDate(dto.getRequestDate());
        entity.setStatus(dto.getStatus());
        entity.setRequestType(dto.getRequestType());
        entity.setProposalFile(dto.getProposalFile());
        entity.setExpectedReturnDate(dto.getExpectedReturnDate());
        return entity;
    }
}
