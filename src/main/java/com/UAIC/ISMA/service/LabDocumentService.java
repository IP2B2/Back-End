package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dto.LabDocumentDTO;
import com.UAIC.ISMA.entity.AccessRequest;
import com.UAIC.ISMA.entity.LabDocument;
import com.UAIC.ISMA.entity.Laboratory;
import com.UAIC.ISMA.exception.EntityNotFoundException;
import com.UAIC.ISMA.exception.LabDocumentNotFoundException;
import com.UAIC.ISMA.mapper.LabDocumentsMapper;
import com.UAIC.ISMA.repository.AccessRequestRepository;
import com.UAIC.ISMA.repository.LabDocumentRepository;
import com.UAIC.ISMA.repository.LaboratoryRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LabDocumentService {

    private static final Logger logger = LoggerFactory.getLogger(LabDocumentService.class);

    private final LabDocumentRepository repository;
    private final LaboratoryRepository labRepo;
    private final AccessRequestRepository accessRequestRepository;
    private final LabDocumentsMapper mapper;
    private final AuditLogService auditLogService;

    @Value("${upload.dir}")
    private String uploadDir;

    public LabDocumentService(LabDocumentRepository repository,
                              LaboratoryRepository labRepo,
                              LabDocumentsMapper mapper,
                              AuditLogService auditLogService,
                              AccessRequestRepository accessRequestRepository) {
        this.repository = repository;
        this.labRepo = labRepo;
        this.mapper = mapper;
        this.auditLogService = auditLogService;
        this.accessRequestRepository = accessRequestRepository;
    }

    public List<LabDocumentDTO> getDocumentsByLab(String labId) {
        Long id = Long.parseLong(labId);
        logger.info("Listare documente pentru laboratorul cu ID: {}", id);
        Laboratory lab = labRepo.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Laboratorul cu id-ul " + labId + " nu există."));

        return repository.findByLab_IdAndArchivedFalse(id)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public LabDocumentDTO storeDocument(MultipartFile file, String labId, String requestId, String version) {
        String originalFilename = file.getOriginalFilename();
        logger.info("Upload de fișier: {} (labId={}, requestId={}, version={})", originalFilename, labId, requestId, version);

        if (originalFilename == null || !isAllowedFileType(originalFilename)) {
            logger.warn("Fișier neacceptat: {}", originalFilename);
            throw new IllegalArgumentException("File type not allowed: " + originalFilename);
        }

        try {
            Files.createDirectories(Paths.get(uploadDir));
            Path filePath = Paths.get(uploadDir).resolve(originalFilename);
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            LabDocument doc = LabDocument.builder()
                    .filename(originalFilename)
                    .fileType(file.getContentType())
                    .version(version != null ? version : "v1.0")
                    .filePath(filePath.toString())
                    .archived(false)
                    .build();

            if (labId != null) {
                Laboratory lab = labRepo.findById(Long.parseLong(labId))
                        .orElseThrow(() -> new RuntimeException("Lab not found: " + labId));
                doc.setLab(lab);
            }

            repository.save(doc);
            logger.info("Document salvat cu ID: {}", doc.getId());
            return mapper.toDTO(doc);
        } catch (Exception e) {
            logger.error("Eroare la salvarea documentului: {}", originalFilename, e);
            throw new RuntimeException("Failed to store file", e);
        }
    }

    public LabDocumentDTO updateDocument(Long id, MultipartFile newFile, String newVersion) {
        logger.info("Update document ID={} cu fișier: {}", id, newFile.getOriginalFilename());

        LabDocument oldDoc = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found: " + id));
        oldDoc.setArchived(true);
        repository.save(oldDoc);
        logger.info("Document vechi arhivat: {}", oldDoc.getId());

        try {
            Path filePath = Paths.get(uploadDir).resolve(newFile.getOriginalFilename());
            Files.copy(newFile.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            LabDocument updated = LabDocument.builder()
                    .filename(newFile.getOriginalFilename())
                    .fileType(newFile.getContentType())
                    .version(newVersion != null ? newVersion : "v-next")
                    .lab(oldDoc.getLab())
                    .filePath(filePath.toString())
                    .archived(false)
                    .build();

            repository.save(updated);
            logger.info("Document nou salvat cu ID: {}", updated.getId());
            return mapper.toDTO(updated);
        } catch (Exception e) {
            logger.error("Eroare la actualizarea documentului ID={}", id, e);
            throw new RuntimeException("Failed to update document", e);
        }
    }

    public ResponseEntity<Resource> downloadDocument(Long id) {
        LabDocument doc = repository.findById(id)
                .orElseThrow(() -> new LabDocumentNotFoundException(id));

        logger.info("Download document: {} (ID={})", doc.getFilename(), id);

        Path filePath = Paths.get(doc.getFilePath());
        Resource resource = new FileSystemResource(filePath.toFile());

        if (!resource.exists()) {
            logger.error("Fișier lipsă pe disc: {}", doc.getFilePath());
            throw new RuntimeException("File not found on disk: " + doc.getFilename());
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getFilename() + "\"")
                .body(resource);
    }

    public void deleteDocument(Long id) {
        LabDocument doc = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found: " + id));

        try {
            Path path = Paths.get(doc.getFilePath());
            Files.deleteIfExists(path);
            logger.info("Fișier șters de pe disc: {}", path);
        } catch (Exception e) {
            logger.warn("Nu s-a putut șterge fișierul fizic: {}", doc.getFilePath(), e);
        }

        repository.delete(doc);
        logger.info("Document șters din DB: ID={}", id);
    }

    private boolean isAllowedFileType(String filename) {
        String lower = filename.toLowerCase();
        return lower.endsWith(".pdf") || lower.endsWith(".docx")
                || lower.endsWith(".jpg") || lower.endsWith(".jpeg")
                || lower.endsWith(".png");
    }
}
