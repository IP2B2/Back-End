package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.LabDocumentDTO;
import com.UAIC.ISMA.service.LabDocumentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LabDocumentControllerTest {

 @Mock
 private LabDocumentService service;

 @InjectMocks
 private LabDocumentController controller;

 private LabDocumentDTO sampleDTO;

 @BeforeEach
 void setUp() {
  MockitoAnnotations.openMocks(this);

  sampleDTO = new LabDocumentDTO();
  sampleDTO.setId(1L);
  sampleDTO.setFilename("document.pdf");
  sampleDTO.setVersion("v1.0");
  sampleDTO.setFileType("application/pdf");
  sampleDTO.setFilePath("uploads/document.pdf");
  sampleDTO.setLabId(1L);
 }

 @Test
 void testGetDocumentsByLab() {
  when(service.getDocumentsByLab("1")).thenReturn(List.of(sampleDTO));

  ResponseEntity<List<LabDocumentDTO>> response = controller.getDocumentsByLab("1");

  assertEquals(200, response.getStatusCodeValue());
  assertEquals(1, response.getBody().size());
  assertEquals("document.pdf", response.getBody().get(0).getFilename());
 }

 @Test
 void testDownloadDocument() {
  Resource resource = new ByteArrayResource("test".getBytes());

  when(service.downloadDocument(1L)).thenReturn(ResponseEntity.ok(resource));

  ResponseEntity<Resource> response = controller.downloadDocument(1L);

  assertEquals(200, response.getStatusCodeValue());
  assertNotNull(response.getBody());
 }

 @Test
 void testUploadLabDocument() {
  MultipartFile mockFile = new MockMultipartFile("file", "test.pdf", "application/pdf", "data".getBytes());

  when(service.storeDocument(any(), eq("1"), isNull(), eq("v1.0"))).thenReturn(sampleDTO);

  ResponseEntity<LabDocumentDTO> response = controller.uploadLabDocument("1", mockFile, "v1.0");

  assertEquals(200, response.getStatusCodeValue());
  assertEquals("document.pdf", response.getBody().getFilename());
 }

 @Test
 void testUploadRequestDocument() {
  MultipartFile mockFile = new MockMultipartFile("file", "test.pdf", "application/pdf", "data".getBytes());

  when(service.storeDocument(any(), isNull(), eq("REQ123"), isNull())).thenReturn(sampleDTO);

  ResponseEntity<LabDocumentDTO> response = controller.uploadRequestDocument("REQ123", mockFile);

  assertEquals(200, response.getStatusCodeValue());
  assertEquals("document.pdf", response.getBody().getFilename());
 }

 @Test
 void testDeleteDocument() {
  doNothing().when(service).deleteDocument(1L);

  ResponseEntity<Void> response = controller.deleteDocument(1L);

  assertEquals(200, response.getStatusCodeValue());
  verify(service, times(1)).deleteDocument(1L);
 }

 @Test
 void testUpdateDocument() {
  MultipartFile newFile = new MockMultipartFile("file", "updated.pdf", "application/pdf", "data".getBytes());

  when(service.updateDocument(eq(1L), any(), eq("v2.0"))).thenReturn(sampleDTO);

  ResponseEntity<LabDocumentDTO> response = controller.updateDocument(1L, newFile, "v2.0");

  assertEquals(200, response.getStatusCodeValue());
  assertEquals("document.pdf", response.getBody().getFilename());
 }
}
