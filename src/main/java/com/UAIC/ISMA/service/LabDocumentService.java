package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dto.LabDocumentDTO;
import com.UAIC.ISMA.entity.LabDocument;
import com.UAIC.ISMA.entity.Laboratory;
import com.UAIC.ISMA.mapper.LabDocumentsMapper;
import com.UAIC.ISMA.repository.LabDocumentRepository;
import com.UAIC.ISMA.repository.LaboratoryRepository;
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

    private final LabDocumentRepository repository;
    private final LaboratoryRepository labRepo;
    private final LabDocumentsMapper mapper;

    @Value("${upload.dir}")
    private String uploadDir;

    public LabDocumentService(LabDocumentRepository repository,
                              LaboratoryRepository labRepo,
                              LabDocumentsMapper mapper) {
        this.repository = repository;
        this.labRepo = labRepo;
        this.mapper = mapper;
    }

    public List<LabDocumentDTO> getDocumentsByLab(String labId) {
        Long id = Long.parseLong(labId);
        return repository.findByLab_IdAndArchivedFalse(id)
                .stream()
                .map(mapper::toDTO)
                .collect(Collectors.toList());
    }

    public LabDocumentDTO storeDocument(MultipartFile file, String labId, String requestId, String version) {
        String originalFilename = file.getOriginalFilename();
        if (originalFilename == null || !isAllowedFileType(originalFilename)) {
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
                        .orElseThrow(() -> new RuntimeException("Lab not found"));
                doc.setLab(lab);
            }

            repository.save(doc);
            return mapper.toDTO(doc);
        } catch (Exception e) {
            throw new RuntimeException("Failed to store file: " + originalFilename, e);
        }
    }

    public ResponseEntity<Resource> downloadDocument(Long id) {
        LabDocument doc = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Document not found with ID: " + id));

        Path filePath = Paths.get(doc.getFilePath());
        Resource resource = new FileSystemResource(filePath.toFile());

        if (!resource.exists()) {
            throw new RuntimeException("File not found on disk: " + doc.getFilename());
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getFilename() + "\"")
                .body(resource);
    }

    public void deleteDocument(Long id) {
        LabDocument doc = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Document not found with ID: " + id));

        try {
            Path filePath = Paths.get(doc.getFilePath());
            Files.deleteIfExists(filePath);
        } catch (Exception e) {
            // Ignoră sau loghează eroarea de fișier
        }

        repository.delete(doc);
    }

    private boolean isAllowedFileType(String filename) {
        String lower = filename.toLowerCase();
        return lower.endsWith(".pdf") || lower.endsWith(".docx")
                || lower.endsWith(".jpg") || lower.endsWith(".jpeg")
                || lower.endsWith(".png");
    }

    public LabDocumentDTO updateDocument(Long id, MultipartFile newFile, String newVersion) {
        LabDocument oldDoc = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));

        oldDoc.setArchived(true);
        repository.save(oldDoc);

        LabDocument updated = LabDocument.builder()
                .filename(newFile.getOriginalFilename())
                .fileType(newFile.getContentType())
                .version(newVersion != null ? newVersion : "v-next")
                .lab(oldDoc.getLab())
                .archived(false)
                .build();

        try {
            Path path = Paths.get(uploadDir).resolve(newFile.getOriginalFilename());
            Files.copy(newFile.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);
            updated.setFilePath(path.toString());
        } catch (Exception e) {
            throw new RuntimeException("Could not save new file", e);
        }

        repository.save(updated);
        return mapper.toDTO(updated);
    }

}
