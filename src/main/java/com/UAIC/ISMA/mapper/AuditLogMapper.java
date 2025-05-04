package com.UAIC.ISMA.mapper;

import com.UAIC.ISMA.dao.AuditLog;
import com.UAIC.ISMA.dao.User;
import com.UAIC.ISMA.dto.AuditLogDTO;
import com.UAIC.ISMA.exception.EntityNotFoundException;
import com.UAIC.ISMA.repository.UserRepository;
import org.springframework.stereotype.Component;

@Component
public class AuditLogMapper {

    private final UserRepository userRepository;

    public AuditLogMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public AuditLog toEntity(AuditLogDTO dto) {
        AuditLog entity = new AuditLog();
        entity.setId(dto.getId());
        entity.setAction(dto.getAction());
        entity.setDetails(dto.getDetails());
        entity.setTimestamp(dto.getTimestamp());

        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + dto.getUserId()));
            entity.setUser(user);
        }

        return entity;
    }

    public AuditLogDTO toDto(AuditLog entity) {
        AuditLogDTO dto = new AuditLogDTO();
        dto.setId(entity.getId());
        dto.setAction(entity.getAction());
        dto.setDetails(entity.getDetails());
        dto.setTimestamp(entity.getTimestamp());
        dto.setUserId(entity.getUser().getId());
        return dto;
    }
}

