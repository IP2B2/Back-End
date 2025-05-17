package com.UAIC.ISMA.service;

import com.UAIC.ISMA.entity.LabDocument;
import com.UAIC.ISMA.dto.LabDocumentDTO;
import com.UAIC.ISMA.exception.LabDocumentNotFoundException;
import com.UAIC.ISMA.mapper.LabDocumentsMapper;
import com.UAIC.ISMA.repository.LabDocumentRepository;
import com.UAIC.ISMA.repository.LaboratoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LabDocumentService {
    public final LabDocumentRepository labDocumentRepository;
    public final LaboratoryRepository laboratoryRepository;

    @Autowired
    public LabDocumentService(LabDocumentRepository labDocumentRepository, LaboratoryRepository laboratoryRepository) {
        this.labDocumentRepository = labDocumentRepository;
        this.laboratoryRepository = laboratoryRepository;
    }

    public List<LabDocumentDTO> getAllDocuments() {
        return labDocumentRepository.findAll().stream().map(LabDocumentsMapper::toDTO).collect(Collectors.toList());
    }

    public LabDocumentDTO findById(Long id) {
        LabDocument labDocument = labDocumentRepository.findById(id)
                .orElseThrow(() -> new LabDocumentNotFoundException(id));
        return LabDocumentsMapper.toDTO(labDocument);
    }

    public List<LabDocumentDTO> getDocumentsByLabId(Long id) {
        laboratoryRepository.findById(id).orElseThrow(() -> new LabDocumentNotFoundException(id));
        return labDocumentRepository.findByLaboratoryId(id).stream().map(LabDocumentsMapper::toDTO).collect(Collectors.toList());
    }

    public LabDocumentDTO createDocument(LabDocumentDTO labDocumentDTO) {
        LabDocument lab = LabDocumentsMapper.toEntity(labDocumentDTO, laboratoryRepository);
        LabDocument savedLab = labDocumentRepository.save(lab);
        return LabDocumentsMapper.toDTO(savedLab);
    }

    public LabDocumentDTO updateLabDocument(Long id, LabDocumentDTO labDocumentDTO) {
        labDocumentRepository.findById(id)
                .orElseThrow(() -> new LabDocumentNotFoundException(id));

        LabDocument labDocument = LabDocumentsMapper.toEntity(labDocumentDTO, laboratoryRepository);
        labDocument.setId(id);
        return LabDocumentsMapper.toDTO(labDocumentRepository.save(labDocument));
    }

    public void deleteLabDocument(Long id) {
        LabDocument labDocument = labDocumentRepository.findById(id)
                .orElseThrow(() -> new LabDocumentNotFoundException(id));
        labDocumentRepository.delete(labDocument);
    }
}

