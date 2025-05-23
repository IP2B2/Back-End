package com.UAIC.ISMA.mapper;

import com.UAIC.ISMA.dto.LabDocumentDTO;
import com.UAIC.ISMA.entity.LabDocument;
import org.springframework.stereotype.Component;

@Component
public class LabDocumentsMapper {

    public LabDocumentDTO toDTO(LabDocument entity) {
        LabDocumentDTO dto = new LabDocumentDTO();
        dto.setId(entity.getId());
        dto.setFilename(entity.getFilename());
        dto.setFileType(entity.getFileType());
        dto.setVersion(entity.getVersion());
        dto.setFilePath(entity.getFilePath());

        if (entity.getLab() != null) {
            dto.setLabId(entity.getLab().getId());
        }

        return dto;
    }

    public LabDocument fromDTO(LabDocumentDTO dto) {
        LabDocument doc = new LabDocument();
        doc.setId(dto.getId());
        doc.setFilename(dto.getFilename());
        doc.setFileType(dto.getFileType());
        doc.setVersion(dto.getVersion());
        doc.setFilePath(dto.getFilePath());
        return doc;
    }
}
