package com.UAIC.ISMA.service;

import com.UAIC.ISMA.controller.LabDocumentController;
import com.UAIC.ISMA.controller.LaboratoryController;
import com.UAIC.ISMA.dao.LabDocument;
import com.UAIC.ISMA.dao.Laboratory;
import com.UAIC.ISMA.dto.LabDocumentDTO;
import com.UAIC.ISMA.dto.NotificationDTO;
import com.UAIC.ISMA.exception.EntityNotFoundException;
import com.UAIC.ISMA.exception.LabDocumentNotFoundException;
import com.UAIC.ISMA.exception.NotificationNotFoundException;
import com.UAIC.ISMA.repository.LabDocumentRepository;
import com.UAIC.ISMA.repository.LaboratoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LabDocumentControllerTest {

 @Mock
 private LabDocumentRepository labDocumentRepository;

 @Mock
 private LabDocumentService labDocumentService;

 @InjectMocks
 private LabDocumentController labDocumentController;


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
 void testGetAllLabDocuments_Succes(){
  LabDocumentDTO labDocumentDTO2 = new LabDocumentDTO();
  labDocumentDTO2.setId(1L);
  labDocumentDTO2.setTitle("Test");
  labDocumentDTO2.setDescription("Desc");
  labDocumentDTO2.setFilePath("test/path.pdf");
  labDocumentDTO2.setUpdatedAt(labDocument.getUpdatedAt());
  labDocumentDTO2.setLaboratoryId(1L);

  when(labDocumentService.getAllDocuments()).thenReturn(List.of(labDocumentDTO, labDocumentDTO2));

  ResponseEntity<List<LabDocumentDTO>> response = labDocumentController.getAllLabDocs(1L);

  assertNotNull(response);
  assertEquals(HttpStatus.OK, response.getStatusCode());
  assertEquals(2, response.getBody().size());
 }

 @Test
 void testGetLabDocumentsById_Success() {
  // Arrange
  when(labDocumentService.findById(1L)).thenReturn(labDocumentDTO);

  // Act
  ResponseEntity<LabDocumentDTO> response = labDocumentController.getLabDocumentsById(1L);

  // Assert
  assertNotNull(response);
  assertEquals(HttpStatus.OK, response.getStatusCode());
 }

 @Test
 void testGetDocsByLabId_LabDocumentNotFound() {
  // Arrange
  when(labDocumentService.getDocumentsByLabId(1L))
          .thenThrow(new LabDocumentNotFoundException(1L));

  // Act & Assert
  assertThrows(LabDocumentNotFoundException.class, () -> labDocumentController.getDocsByLabId(1L));
 }

 @Test
 void testCreateLabDocument_Success() {
  // Arrange
  when(labDocumentService.createDocument(labDocumentDTO)).thenReturn(labDocumentDTO);

  // Act
  LabDocumentDTO result = labDocumentController.createLabDocument(labDocumentDTO);

  // Assert
  assertNotNull(result);
  assertEquals(labDocumentDTO.getId(), result.getId());
  assertEquals(labDocumentDTO.getTitle(), result.getTitle());
 }

 @Test
 void testUpdateLabDocument_Success() {
  // Arrange
  LabDocumentDTO updatedDTO = new LabDocumentDTO();
  updatedDTO.setId(1L);
  updatedDTO.setTitle("Updated Title");
  updatedDTO.setDescription("Updated Description");
  updatedDTO.setFilePath("updated/path.pdf");
  updatedDTO.setLaboratoryId(1L);

  when(labDocumentService.updateLabDocument(1L, updatedDTO)).thenReturn(updatedDTO);

  // Act
  ResponseEntity<LabDocumentDTO> response = labDocumentController.updateLabDoc(1L, updatedDTO);

  // Assert
  assertNotNull(response);
  assertEquals(HttpStatus.OK, response.getStatusCode());
  assertEquals("Updated Title", response.getBody().getTitle());
  assertEquals("Updated Description", response.getBody().getDescription());
 }

 @Test
 void testUpdateLabDocuments_LabDocumentsNotFound() {
  // Arrange
  when(labDocumentService.updateLabDocument(1L, labDocumentDTO)).thenThrow(LabDocumentNotFoundException.class);

  // Act & Assert
  assertThrows(LabDocumentNotFoundException.class, () -> labDocumentController.updateLabDoc(1L, labDocumentDTO));
 }

 @Test
 void testDeleteNotification_Success() {
  // Arrange
  doNothing().when(labDocumentService).deleteLabDocument(1L);

  // Act
  ResponseEntity<Void> response = labDocumentController.deleteLabDoc(1L);

  // Assert
  assertNotNull(response);
  assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
 }

 @Test
 void testDeleteNotification_NotificationNotFound() {
  // Arrange
  doThrow(NotificationNotFoundException.class).when(labDocumentService).deleteLabDocument(1L);

  // Act and Assert
  assertThrows(NotificationNotFoundException.class, () -> labDocumentController.deleteLabDoc(1L));
 }
}