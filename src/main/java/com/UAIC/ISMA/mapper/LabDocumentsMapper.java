package com.UAIC.ISMA.mapper;

import com.UAIC.ISMA.entity.LabDocument;
import com.UAIC.ISMA.entity.Laboratory;
import com.UAIC.ISMA.dto.LabDocumentDTO;
import com.UAIC.ISMA.exception.EntityNotFoundException;
import com.UAIC.ISMA.repository.LaboratoryRepository;

public class LabDocumentsMapper {

    public static LabDocumentDTO toDTO(LabDocument labDocument) {
        LabDocumentDTO dto = new LabDocumentDTO();
        dto.setId(labDocument.getId());
        dto.setDescription(labDocument.getDescription());
        dto.setTitle(labDocument.getTitle());
        dto.setFilePath(labDocument.getFilePath());
        dto.setUpdatedAt(labDocument.getUpdatedAt());
        dto.setLaboratoryId(labDocument.getLaboratory().getId());
        return dto;
    }

    public static LabDocument toEntity(LabDocumentDTO labDocumentDTO, LaboratoryRepository labRepo) {
        LabDocument labDocument = new LabDocument();
        labDocument.setId(labDocumentDTO.getId());
        labDocument.setTitle(labDocumentDTO.getTitle());
        labDocument.setDescription(labDocumentDTO.getDescription());
        labDocument.setFilePath(labDocumentDTO.getFilePath());
        labDocument.setUpdatedAt(labDocumentDTO.getUpdatedAt());

        Laboratory laboratory = labRepo.findById(labDocumentDTO.getLaboratoryId())
                .orElseThrow(() -> new EntityNotFoundException("Nu s-a gasit laboratorul cu id-ul: " + labDocumentDTO.getLaboratoryId()));
        labDocument.setLaboratory(laboratory);
        return labDocument;
    }
}
