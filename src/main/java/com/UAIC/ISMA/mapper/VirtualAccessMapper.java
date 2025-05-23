package com.UAIC.ISMA.mapper;

import com.UAIC.ISMA.entity.VirtualAccess;
import com.UAIC.ISMA.dto.VirtualAccessDTO;
import com.UAIC.ISMA.entity.AccessRequest;

public class VirtualAccessMapper {

    public static VirtualAccessDTO toDTO(VirtualAccess entity) {
        return new VirtualAccessDTO(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getIssuedDate(),
                entity.getAccessRequest() != null ? entity.getAccessRequest().getId() : null
        );
    }

    public static VirtualAccess toEntity(VirtualAccessDTO dto) {
        VirtualAccess entity = new VirtualAccess();
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setIssuedDate(dto.getIssuedDate());
        return entity;
    }
}
