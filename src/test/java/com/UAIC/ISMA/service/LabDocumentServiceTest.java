package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dto.LabDocumentDTO;
import com.UAIC.ISMA.entity.LabDocument;
import com.UAIC.ISMA.entity.Laboratory;
import com.UAIC.ISMA.mapper.LabDocumentsMapper;
import com.UAIC.ISMA.repository.LabDocumentRepository;
import com.UAIC.ISMA.repository.LaboratoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mock.web.MockMultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = LabDocumentService.class)
class LabDocumentServiceTest {

    @Autowired
    private LabDocumentService labDocumentService;

    @MockBean
    private LabDocumentRepository repository;

    @MockBean
    private LaboratoryRepository labRepo;

    @MockBean
    private LabDocumentsMapper mapper;

    @MockBean
    private AuditLogService auditLogService;

    private Laboratory lab;
    private LabDocument savedDoc;

    @BeforeEach
    void setup() {
        lab = new Laboratory();
        lab.setId(1L);

        savedDoc = LabDocument.builder()
                .id(1L)
                .filename("test.pdf")
                .filePath("uploads/test.pdf")
                .lab(lab)
                .version("v1.0")
                .fileType("application/pdf")
                .archived(false)
                .build();
    }

    @Test
    void storeDocument_shouldSaveSuccessfully() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "content".getBytes());

        when(labRepo.findById(1L)).thenReturn(Optional.of(lab));
        when(repository.save(any())).thenReturn(savedDoc);
        when(mapper.toDTO(any())).thenReturn(new LabDocumentDTO());

        LabDocumentDTO result = labDocumentService.storeDocument(file, "1", null, "v1.0");

        assertNotNull(result);
        verify(repository, times(1)).save(any());
    }

    @Test
    void updateDocument_shouldArchiveOldAndSaveNew() throws Exception {
        MockMultipartFile file = new MockMultipartFile("file", "new.pdf", "application/pdf", "newcontent".getBytes());

        LabDocument oldDoc = LabDocument.builder()
                .id(1L)
                .filename("old.pdf")
                .filePath("uploads/old.pdf")
                .lab(lab)
                .version("v1.0")
                .fileType("application/pdf")
                .archived(false)
                .build();

        when(repository.findById(1L)).thenReturn(Optional.of(oldDoc));
        when(repository.save(any())).thenReturn(savedDoc);
        when(mapper.toDTO(any())).thenReturn(new LabDocumentDTO());

        LabDocumentDTO result = labDocumentService.updateDocument(1L, file, "v2.0");

        assertNotNull(result);
        verify(repository, times(2)).save(any()); // once for archived, once for new
    }

    @Test
    void deleteDocument_shouldDeleteSuccessfully() throws Exception {
        LabDocument doc = savedDoc;
        when(repository.findById(1L)).thenReturn(Optional.of(doc));

        labDocumentService.deleteDocument(1L);

        verify(repository).delete(doc);
    }
}
