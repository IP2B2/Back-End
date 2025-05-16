package com.UAIC.ISMA.service;

import com.UAIC.ISMA.entity.LabDocument;
import com.UAIC.ISMA.entity.Laboratory;
import com.UAIC.ISMA.dto.LabDocumentDTO;
import com.UAIC.ISMA.exception.EntityNotFoundException;
import com.UAIC.ISMA.exception.LabDocumentNotFoundException;
import com.UAIC.ISMA.repository.LabDocumentRepository;
import com.UAIC.ISMA.repository.LaboratoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LabDocumentServiceTest {

    @Mock
    private LabDocumentRepository labDocumentRepository;

    @Mock
    private LaboratoryRepository laboratoryRepository;

    @InjectMocks
    private LabDocumentService labDocumentService;

    private LabDocument labDocument;
    private LabDocumentDTO labDocumentDTO;
    private Laboratory lab;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        lab = new Laboratory();
        lab.setId(1L);

        labDocument = new LabDocument();
        labDocument.setId(1L);
        labDocument.setTitle("Test");
        labDocument.setDescription("Desc");
        labDocument.setFilePath("test/path.pdf");
        labDocument.setUpdatedAt(LocalDateTime.now());
        labDocument.setLaboratory(lab);

        labDocumentDTO = new LabDocumentDTO();
        labDocumentDTO.setId(1L);
        labDocumentDTO.setTitle("Test");
        labDocumentDTO.setDescription("Desc");
        labDocumentDTO.setFilePath("test/path.pdf");
        labDocumentDTO.setUpdatedAt(labDocument.getUpdatedAt());
        labDocumentDTO.setLaboratoryId(1L);
    }

    @Test
    void testFindById_Success() {
        when(labDocumentRepository.findById(1L)).thenReturn(Optional.of(labDocument));

        LabDocumentDTO result = labDocumentService.findById(1L);

        assertNotNull(result);
        assertEquals(labDocument.getId(), result.getId());
        assertEquals(labDocument.getTitle(), result.getTitle());
        assertEquals(labDocument.getDescription(), result.getDescription());
        assertEquals(labDocument.getFilePath(), result.getFilePath());
    }

    @Test
    void testFindById_LabDocumentNotFound() {
        when(labDocumentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(LabDocumentNotFoundException.class, () -> labDocumentService.findById(1L));
    }

    @Test
    void testCreate_Succes() {
        when(laboratoryRepository.findById(1L)).thenReturn(Optional.of(lab));
        when(labDocumentRepository.save(any(LabDocument.class))).thenReturn(labDocument);

        LabDocumentDTO result = labDocumentService.createDocument(labDocumentDTO);

        assertNotNull(result);
        assertEquals(labDocument.getId(), result.getId());
        assertEquals(labDocument.getTitle(), result.getTitle());
        assertEquals(labDocument.getDescription(), result.getDescription());
        assertEquals(labDocument.getFilePath(), result.getFilePath());
        assertEquals(lab.getId(), result.getLaboratoryId());
    }

    @Test
    void testCreate_Fail_NullLabDocumentDTO() {
        assertThrows(NullPointerException.class, () -> labDocumentService.createDocument(null));
    }

    @Test
    void testCreate_Fail_LaboratoryNotFound() {
        when(laboratoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> labDocumentService.createDocument(labDocumentDTO));
    }

    @Test
    void testUpdate_Success() {
        when(labDocumentRepository.findById(1L)).thenReturn(Optional.of(labDocument));
        when(laboratoryRepository.findById(1L)).thenReturn(Optional.of(lab));
        when(labDocumentRepository.save(any(LabDocument.class))).thenReturn(labDocument);

        LabDocumentDTO result = labDocumentService.updateLabDocument(1L, labDocumentDTO);

        assertNotNull(result);
        assertEquals(labDocument.getId(), result.getId());
        assertEquals(labDocument.getTitle(), result.getTitle());
        assertEquals(labDocument.getDescription(), result.getDescription());
        assertEquals(labDocument.getFilePath(), result.getFilePath());
    }

    @Test
    void testUpdate_Fail_DocumentNotFound() {
        when(labDocumentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(LabDocumentNotFoundException.class, () ->
                labDocumentService.updateLabDocument(1L, labDocumentDTO)
        );
    }

    @Test
    void testUpdate_Fail_LaboratoryNotFound() {
        when(labDocumentRepository.findById(1L)).thenReturn(Optional.of(labDocument));
        when(laboratoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                labDocumentService.updateLabDocument(1L, labDocumentDTO)
        );
    }

    @Test
    void testUpdate_Fail_NullLabDocumentDTO(){
        when(labDocumentRepository.findById(1L)).thenReturn(Optional.of(labDocument));

        assertThrows(NullPointerException.class, () -> labDocumentService.updateLabDocument(1L, null));
    }

    @Test
    void testDelete_Success(){
        when(labDocumentRepository.findById(1L)).thenReturn(Optional.of(labDocument));

        labDocumentService.deleteLabDocument(1L);

        verify(labDocumentRepository,times(1)).delete(labDocument);
    }

    @Test
    void testDelete_Fail_NotificationNotFound() {
        when(labDocumentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(LabDocumentNotFoundException.class, () -> labDocumentService.deleteLabDocument(1L));
    }

    @Test
    void testGetAllDocuments_Succes(){
        LabDocument labDocument2 = new LabDocument();
        labDocument2.setId(2L);
        labDocument2.setTitle("Test");
        labDocument2.setDescription("Desc");
        labDocument2.setFilePath("test/path.pdf");
        labDocument2.setUpdatedAt(LocalDateTime.now());
        labDocument2.setLaboratory(lab);

        when(labDocumentRepository.findAll()).thenReturn(List.of(labDocument, labDocument2));

        List<LabDocumentDTO> result = labDocumentService.getAllDocuments();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(labDocument.getId(), result.get(0).getId());
        assertEquals(labDocument2.getId(), result.get(1).getId());
    }

    @Test
    void testFindAll_Fail_EmptyList() {
        when(labDocumentRepository.findAll()).thenReturn(List.of());

        List<LabDocumentDTO> result = labDocumentService.getAllDocuments();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
