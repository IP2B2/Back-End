package com.UAIC.ISMA.mapper;

import com.UAIC.ISMA.dao.RequestDocument;
import com.UAIC.ISMA.dto.RequestDocumentDTO;

public class RequestDocumentMapper {

    public static RequestDocumentDTO toDTO(RequestDocument entity) {
        return new RequestDocumentDTO(
                entity.getId(),
                entity.getTitle(),
                entity.getDescription(),
                entity.getFilePath(),
                entity.getUploadedAt()
        );
    }

    public static RequestDocument toEntity(RequestDocumentDTO dto) {
        RequestDocument entity = new RequestDocument();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setFilePath(dto.getFilePath());
        entity.setUploadedAt(dto.getUploadedAt());
        return entity;
    }
}
