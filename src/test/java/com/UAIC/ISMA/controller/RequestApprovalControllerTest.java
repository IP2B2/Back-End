package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.RequestApprovalDTO;
import com.UAIC.ISMA.exception.RequestApprovalNotFoundException;
import com.UAIC.ISMA.service.RequestApprovalService;
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

class RequestApprovalControllerTest {

    @Mock
    private RequestApprovalService requestApprovalService;

    @InjectMocks
    private RequestApprovalController requestApprovalController;

    private RequestApprovalDTO requestApprovalDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestApprovalDTO = new RequestApprovalDTO();
        requestApprovalDTO.setId(1L);
        requestApprovalDTO.setApprovalStatus("NEEDS_MORE_INFO");
        requestApprovalDTO.setApprovalDate(LocalDateTime.parse("2025-05-08T21:48:13.828602"));
        requestApprovalDTO.setComments("Test comment");
    }

    @Test
    void testGetAllRequestApprovals_Success() {
        // Arrange
        RequestApprovalDTO requestApprovalDTO2 = new RequestApprovalDTO();
        requestApprovalDTO2.setId(2L);
        requestApprovalDTO2.setApprovalStatus("APPROVED");
        requestApprovalDTO2.setApprovalDate(LocalDateTime.parse("2025-05-08T21:48:13.828602"));
        requestApprovalDTO2.setComments("Test comment 2");

        when(requestApprovalService.findAll(1L, 1L)).thenReturn(List.of(requestApprovalDTO, requestApprovalDTO2));

        // Act
        ResponseEntity<List<RequestApprovalDTO>> response = requestApprovalController.getAllRequestApprovals(1L, 1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Test comment", response.getBody().get(0).getComments());
    }

    @Test
    void testGetAllRequestApprovals_EmptyList() {
        // Arrange
        when(requestApprovalService.findAll(1L, 1L)).thenReturn(List.of());

        // Act
        ResponseEntity<List<RequestApprovalDTO>> response = requestApprovalController.getAllRequestApprovals(1L, 1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testGetRequestApprovalById_Success() {
        // Arrange
        when(requestApprovalService.findById(1L)).thenReturn(requestApprovalDTO);

        // Act
        ResponseEntity<RequestApprovalDTO> response = requestApprovalController.getRequestApprovalById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test comment", response.getBody().getComments());
    }

    @Test
    void testGetRequestApprovalById_RequestApprovalNotFound() {
        // Arrange
        when(requestApprovalService.findById(1L)).thenThrow(RequestApprovalNotFoundException.class);

        // Act & Assert
        assertThrows(RequestApprovalNotFoundException.class, () -> requestApprovalController.getRequestApprovalById(1L));
    }

    @Test
    void testCreateRequestApproval_Success() {
        // Arrange
        when(requestApprovalService.create(requestApprovalDTO)).thenReturn(requestApprovalDTO);

        // Act
        ResponseEntity<RequestApprovalDTO> response = requestApprovalController.createRequestApproval(requestApprovalDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Test comment", response.getBody().getComments());
    }

    @Test
    void testUpdateRequestApproval_Success() {
        // Arrange
        RequestApprovalDTO updatedRequestApproval = new RequestApprovalDTO();
        updatedRequestApproval.setId(1L);
        updatedRequestApproval.setApprovalStatus("APPROVED");
        updatedRequestApproval.setApprovalDate(LocalDateTime.parse("2025-05-08T21:48:13.828602"));
        updatedRequestApproval.setComments("Updated comment");

        when(requestApprovalService.update(1L, updatedRequestApproval)).thenReturn(updatedRequestApproval);

        // Act
        ResponseEntity<RequestApprovalDTO> response = requestApprovalController.updateRequestApproval(1L, updatedRequestApproval);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated comment", response.getBody().getComments());
    }

    @Test
    void testUpdateRequestApproval_RequestApprovalNotFound() {
        // Arrange
        when(requestApprovalService.update(1L, requestApprovalDTO)).thenThrow(RequestApprovalNotFoundException.class);

        // Act & Assert
        assertThrows(RequestApprovalNotFoundException.class, () -> requestApprovalController.updateRequestApproval(1L, requestApprovalDTO));
    }

    @Test
    void testDeleteRequestApproval_Success() {
        // Arrange
        doNothing().when(requestApprovalService).delete(1L);

        // Act
        ResponseEntity<Void> response = requestApprovalController.deleteRequestApproval(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteRequestApproval_RequestApprovalNotFound() {
        // Arrange
        doThrow(RequestApprovalNotFoundException.class).when(requestApprovalService).delete(1L);

        // Act and Assert
        assertThrows(RequestApprovalNotFoundException.class, () -> requestApprovalController.deleteRequestApproval(1L));
    }
}
