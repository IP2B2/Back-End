package com.UAIC.ISMA.mapper;

import com.UAIC.ISMA.dao.VirtualAccess;
import com.UAIC.ISMA.dto.VirtualAccessDTO;

public class VirtualAccessMapper {
    public static VirtualAccessDTO toDTO(VirtualAccess entity) {
        return new VirtualAccessDTO(
                entity.getId(),
                entity.getUsername(),
                entity.getPassword(),
                entity.getIssuedDate()
        );
    }

    public static VirtualAccess toEntity(VirtualAccessDTO dto){
        VirtualAccess entity = new VirtualAccess();
        entity.setUsername(dto.getUsername());
        entity.setPassword(dto.getPassword());
        entity.setIssuedDate(dto.getIssuedDate());
        return entity;
    }
}
