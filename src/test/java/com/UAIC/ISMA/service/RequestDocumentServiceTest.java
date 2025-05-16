package com.UAIC.ISMA.service;

import com.UAIC.ISMA.entity.RequestDocument;
import com.UAIC.ISMA.dto.RequestDocumentDTO;
import com.UAIC.ISMA.exception.RequestDocumentNotFoundException;
import com.UAIC.ISMA.repository.RequestDocumentRepository;
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

class RequestDocumentServiceTest {

    @Mock
    private RequestDocumentRepository documentRepository;

    @InjectMocks
    private RequestDocumentService requestDocumentService;

    private RequestDocument document;
    private RequestDocumentDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        document = new RequestDocument();
        document.setId(1L);
        document.setTitle("Test Doc");
        document.setDescription("Desc");
        document.setFilePath("/file.pdf");
        document.setUploadedAt(LocalDateTime.now());


        dto = new RequestDocumentDTO();
        dto.setId(1L);
        dto.setTitle("Test Doc");
        dto.setDescription("Desc");
        dto.setFilePath("/file.pdf");
        dto.setUploadedAt(LocalDateTime.now());

    }

    @Test
    void testFindById_Success() {
        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));

        RequestDocumentDTO result = requestDocumentService.findById(1L);

        assertNotNull(result);
        assertEquals("Test Doc", result.getTitle());
        assertEquals(document.getId(), result.getId());
        assertEquals(document.getFilePath(), result.getFilePath());
        assertEquals(document.getDescription(), result.getDescription());
    }

    @Test
    void testFindById_NotFound() {
        when(documentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RequestDocumentNotFoundException.class, () -> requestDocumentService.findById(1L));
    }

    @Test
    void testFindAll() {
        when(documentRepository.findAll()).thenReturn(List.of(document));

        List<RequestDocumentDTO> result = requestDocumentService.findAll(1L, 2L);

        assertEquals(1, result.size());
    }

    @Test
    void testCreate_Success() {
        when(documentRepository.save(any(RequestDocument.class))).thenReturn(document);

        RequestDocumentDTO result = requestDocumentService.create(dto);

        assertNotNull(result);
        assertEquals("Test Doc", result.getTitle());
        assertEquals(document.getId(), result.getId());
        assertEquals(document.getFilePath(), result.getFilePath());

    }

    @Test
    void testUpdate_Success() {
        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));
        when(documentRepository.save(any(RequestDocument.class))).thenReturn(document);

        dto.setTitle("Updated Doc");
        RequestDocumentDTO result = requestDocumentService.update(1L, dto);

        assertNotNull(result);
        assertEquals("Updated Doc", result.getTitle());
    }

    @Test
    void testUpdate_NotFound() {
        when(documentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RequestDocumentNotFoundException.class, () -> requestDocumentService.update(1L, dto));
    }

    @Test
    void testDelete_Success() {
        when(documentRepository.findById(1L)).thenReturn(Optional.of(document));

        requestDocumentService.delete(1L);

        verify(documentRepository, times(1)).delete(document);
    }

    @Test
    void testDelete_NotFound() {
        when(documentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RequestDocumentNotFoundException.class, () -> requestDocumentService.delete(1L));
    }
}
