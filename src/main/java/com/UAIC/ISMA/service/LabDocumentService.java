package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dao.LabDocument;
import com.UAIC.ISMA.dao.Laboratory;
import com.UAIC.ISMA.dto.LabDocumentDTO;
import com.UAIC.ISMA.exception.EntityNotFoundException;
import com.UAIC.ISMA.repository.LabDocumentRepository;
import com.UAIC.ISMA.repository.LaboratoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LabDocumentService {
    public final LabDocumentRepository labDocumentRepository;
    public final LaboratoryRepository laboratoryRepository;

    public LabDocumentService(LabDocumentRepository labDocumentRepository, LaboratoryRepository laboratoryRepository) {
        this.labDocumentRepository = labDocumentRepository;
        this.laboratoryRepository = laboratoryRepository;
    }

    public List<LabDocumentDTO> getAllDocuments() {
        return labDocumentRepository.findAll().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public Optional<LabDocumentDTO> getDocumentsById(Long id) {
        return labDocumentRepository.findById(id).map(this::convertToDTO);
    }

    public List<LabDocumentDTO> getDocumentsByLabId(Long id) {
        laboratoryRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Nu exista Laboratorul cu Id-ul: " + id));
        return labDocumentRepository.findByLaboratoryId(id).stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    public LabDocumentDTO createDocument(LabDocumentDTO labDocumentDTO) {
        LabDocument lab = convertToEntity(labDocumentDTO);
        LabDocument savedLab = labDocumentRepository.save(lab);
        return convertToDTO(savedLab);
    }


    public LabDocumentDTO updateLabDocument(Long id, LabDocumentDTO labDocumentDTO) {
        LabDocument lab = labDocumentRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Nu exista Documentul cu Id-ul: " + id));
        LabDocument labDocument = convertToEntity(labDocumentDTO);
        labDocument.setId(id);
        return convertToDTO(labDocumentRepository.save(labDocument));
    }

    public void deleteLabDocument(Long id) {
        LabDocument labDocument = labDocumentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Nu exista Documentul cu Id-ul: " + id));
        labDocumentRepository.delete(labDocument);
    }


    private LabDocumentDTO convertToDTO(LabDocument labDocument) {
        LabDocumentDTO dto = new LabDocumentDTO();
        dto.setId(labDocument.getId());
        dto.setDescription(labDocument.getDescription());
        dto.setTitle(labDocument.getTitle());
        dto.setFilePath(labDocument.getFilePath());
        dto.setUpdatedAt(labDocument.getUpdatedAt());
        dto.setLaboratoryId(labDocument.getLaboratory().getId());

        return dto;
    }

    private LabDocument convertToEntity(LabDocumentDTO labDocumentDTO) {
        LabDocument labDocument = new LabDocument();
        labDocument.setId(labDocumentDTO.getId());
        labDocument.setTitle(labDocumentDTO.getTitle());
        labDocument.setDescription(labDocumentDTO.getDescription());
        labDocument.setFilePath(labDocumentDTO.getFilePath());
        labDocument.setUpdatedAt(labDocumentDTO.getUpdatedAt());

        Laboratory laboratory = laboratoryRepository.findById(labDocumentDTO.getLaboratoryId())
                .orElseThrow(() -> new EntityNotFoundException("Nu s-a gasit laboratorul cu id-ul: " + labDocumentDTO.getLaboratoryId()));

        labDocument.setLaboratory(laboratory);
        return labDocument;
    }
}
