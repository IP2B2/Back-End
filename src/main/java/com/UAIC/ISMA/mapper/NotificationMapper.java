package com.UAIC.ISMA.mapper;

import com.UAIC.ISMA.dao.Notification;
import com.UAIC.ISMA.dto.NotificationDTO;

public class NotificationMapper {

    public static NotificationDTO toDTO(Notification entity) {
        return new NotificationDTO(
                entity.getId(),
                entity.getMessage(),
                entity.getCreatedDate(),
                entity.getReadStatus()
        );
    }

    public static Notification toEntity(NotificationDTO dto) {
        Notification entity = new Notification();
        entity.setMessage(dto.getMessage());
        entity.setCreatedDate(dto.getCreatedDate());
        entity.setReadStatus(dto.getReadStatus());
        return entity;
    }
}
