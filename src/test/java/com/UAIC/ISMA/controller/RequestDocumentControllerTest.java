package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.NotificationDTO;
import com.UAIC.ISMA.dto.RequestDocumentDTO;
import com.UAIC.ISMA.exception.NotificationNotFoundException;
import com.UAIC.ISMA.exception.RequestDocumentNotFoundException;
import com.UAIC.ISMA.service.RequestDocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RequestDocumentControllerTest {

    @Mock
    private RequestDocumentService requestDocumentService;

    @InjectMocks
    private RequestDocumentController requestDocumentController;

    private RequestDocumentDTO requestDocumentDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestDocumentDTO = new RequestDocumentDTO();
        requestDocumentDTO.setId(1L);
        requestDocumentDTO.setTitle("Test Doc");
        requestDocumentDTO.setDescription("Desc");
        requestDocumentDTO.setFilePath("/file.pdf");
        requestDocumentDTO.setUploadedAt(LocalDateTime.now());

    }

    @Test
    void testGetAllDocuments() {
        //Arrange
        RequestDocumentDTO requestDocumentDTO2 = new RequestDocumentDTO();
        requestDocumentDTO2.setId(2L);
        requestDocumentDTO2.setTitle("Test Doc2");
        requestDocumentDTO2.setDescription("Desc2");
        requestDocumentDTO2.setFilePath("/file2.pdf");
        requestDocumentDTO2.setUploadedAt(LocalDateTime.now());

        when(requestDocumentService.findAll(1L, 2L)).thenReturn(List.of(requestDocumentDTO,requestDocumentDTO2 ));

        //Act
        ResponseEntity<List<RequestDocumentDTO>> response = requestDocumentController.getAllDocuments(1L, 2L );

        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, response.getBody().size());
        assertEquals("Test Doc", response.getBody().get(0).getTitle());
    }

    @Test
    void testGetDocumentById_Success() {
        //Arrange

        when(requestDocumentService.findById(1L)).thenReturn(requestDocumentDTO);
        //Act
        ResponseEntity<RequestDocumentDTO> response = requestDocumentController.getById(1L);
        // Assert
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Test Doc", response.getBody().getTitle());
    }

    @Test
    void testGetDocumentById_NotFound() {
        when(requestDocumentService.findById(1L)).thenThrow(RequestDocumentNotFoundException.class);

        assertThrows(RequestDocumentNotFoundException.class, () -> requestDocumentController.getById(1L));
    }

    @Test
    void testCreateDocument_Success() {
        when(requestDocumentService.create(requestDocumentDTO)).thenReturn(requestDocumentDTO);

        ResponseEntity<RequestDocumentDTO> response = requestDocumentController.create(requestDocumentDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Test Doc", response.getBody().getTitle());
    }

    @Test
    void testUpdateDocument_Success() {
        RequestDocumentDTO updatedDocument = new RequestDocumentDTO();
        requestDocumentDTO.setId(1L);
        requestDocumentDTO.setTitle("Updated Title");

        when(requestDocumentService.update(1L, requestDocumentDTO)).thenReturn(requestDocumentDTO);

        ResponseEntity<RequestDocumentDTO> response = requestDocumentController.update(1L, requestDocumentDTO);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Updated Title", response.getBody().getTitle());
    }

    @Test
    void testUpdateNotification_NotificationNotFound() {
        // Arrange
        when(requestDocumentService.update(1L, requestDocumentDTO)).thenThrow(NotificationNotFoundException.class);

        // Act & Assert
        assertThrows(NotificationNotFoundException.class, () -> requestDocumentController.update(1L, requestDocumentDTO));
    }

    @Test
    void testDeleteDocument_Success() {

        doNothing().when(requestDocumentService).delete(1L);
        //Act
        ResponseEntity<Void> response = requestDocumentController.delete(1L);
        //Asert
        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void testDeleteDocument_NotificationNotFound() {
        // Arrange
        doThrow(NotificationNotFoundException.class).when(requestDocumentService).delete(1L);

        // Act and Assert
        assertThrows(NotificationNotFoundException.class, () -> requestDocumentController.delete(1L));
    }


    @Test
    void testGetAllNotifications_EmptyList() {
        // Arrange
        when(requestDocumentService.findAll(1L, 2L)).thenReturn(List.of());

        // Act
        ResponseEntity<List<RequestDocumentDTO>> response = requestDocumentController.getAllDocuments(1L, 2L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

}
