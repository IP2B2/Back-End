package com.UAIC.ISMA.mapper;

import com.UAIC.ISMA.dao.AuditLog;
import com.UAIC.ISMA.dao.User;
import com.UAIC.ISMA.dto.AuditLogDTO;
import org.springframework.stereotype.Component;

@Component
public class AuditLogMapper {

    public AuditLog toEntity(AuditLogDTO dto, User user) {
        AuditLog entity = new AuditLog();
        entity.setId(dto.getId());
        entity.setAction(dto.getAction());
        entity.setDetails(dto.getDetails());
        entity.setTimestamp(dto.getTimestamp());
        entity.setUser(user);
        return entity;
    }

    public AuditLogDTO toDto(AuditLog entity) {
        AuditLogDTO dto = new AuditLogDTO();
        dto.setId(entity.getId());
        dto.setAction(entity.getAction());
        dto.setDetails(entity.getDetails());
        dto.setTimestamp(entity.getTimestamp());
        if (entity.getUser() != null) {
            dto.setUserId(entity.getUser().getId());
        }
        return dto;
    }
}

