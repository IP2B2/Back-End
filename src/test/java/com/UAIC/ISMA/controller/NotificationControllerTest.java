package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.NotificationDTO;
import com.UAIC.ISMA.exception.NotificationNotFoundException;
import com.UAIC.ISMA.service.NotificationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class NotificationControllerTest {

    @Mock
    private NotificationService notificationService;

    @InjectMocks
    private NotificationController notificationController;

    private NotificationDTO notificationDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        notificationDTO = new NotificationDTO();
        notificationDTO.setId(1L);
        notificationDTO.setMessage("Test message");
        notificationDTO.setReadStatus(false);
    }

    @Test
    void testGetAllNotifications_Success() {
        // Arrange
        NotificationDTO notificationDTO2 = new NotificationDTO();
        notificationDTO2.setId(2L);
        notificationDTO2.setMessage("Message 2");
        notificationDTO2.setReadStatus(true);

        when(notificationService.findAll(1L)).thenReturn(List.of(notificationDTO, notificationDTO2));

        // Act
        ResponseEntity<List<NotificationDTO>> response = notificationController.getAllNotifications(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals("Test message", response.getBody().get(0).getMessage());
    }

    @Test
    void testGetAllNotifications_EmptyList() {
        // Arrange
        when(notificationService.findAll(1L)).thenReturn(List.of());

        // Act
        ResponseEntity<List<NotificationDTO>> response = notificationController.getAllNotifications(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testGetNotificationById_Success() {
        // Arrange
        when(notificationService.findById(1L)).thenReturn(notificationDTO);

        // Act
        ResponseEntity<NotificationDTO> response = notificationController.getNotificationById(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Test message", response.getBody().getMessage());
    }

    @Test
    void testGetNotificationById_NotificationNotFound() {
        // Arrange
        when(notificationService.findById(1L)).thenThrow(NotificationNotFoundException.class);

        // Act & Assert
        assertThrows(NotificationNotFoundException.class, () -> notificationController.getNotificationById(1L));
    }

    @Test
    void testCreateNotification_Success() {
        // Arrange
        when(notificationService.create(notificationDTO)).thenReturn(notificationDTO);

        // Act
        ResponseEntity<NotificationDTO> response = notificationController.createNotification(notificationDTO);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Test message", response.getBody().getMessage());
    }

    @Test
    void testUpdateNotification_Success() {
        // Arrange
        NotificationDTO updatedNotification = new NotificationDTO();
        updatedNotification.setId(1L);
        updatedNotification.setMessage("Updated message");
        updatedNotification.setReadStatus(true);

        when(notificationService.update(1L, updatedNotification)).thenReturn(updatedNotification);

        // Act
        ResponseEntity<NotificationDTO> response = notificationController.updateNotification(1L, updatedNotification);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated message", response.getBody().getMessage());
    }

    @Test
    void testUpdateNotification_NotificationNotFound() {
        // Arrange
        when(notificationService.update(1L, notificationDTO)).thenThrow(NotificationNotFoundException.class);

        // Act & Assert
        assertThrows(NotificationNotFoundException.class, () -> notificationController.updateNotification(1L, notificationDTO));
    }

    @Test
    void testDeleteNotification_Success() {
        // Arrange
        doNothing().when(notificationService).delete(1L);

        // Act
        ResponseEntity<Void> response = notificationController.deleteNotification(1L);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteNotification_NotificationNotFound() {
        // Arrange
        doThrow(NotificationNotFoundException.class).when(notificationService).delete(1L);

        // Act and Assert
        assertThrows(NotificationNotFoundException.class, () -> notificationController.deleteNotification(1L));
    }


}
