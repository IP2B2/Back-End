package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.LaboratoryDTO;
import com.UAIC.ISMA.exception.LaboratoryNotFoundException;
import com.UAIC.ISMA.service.LaboratoryService;
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

public class LaboratoryControllerTest {

    @Mock
    private LaboratoryService laboratoryService;

    @InjectMocks
    private LaboratoryController laboratoryController;

    private LaboratoryDTO laboratoryDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        laboratoryDTO = new LaboratoryDTO();
        laboratoryDTO.setId(1L);
        laboratoryDTO.setLabName("Lab A");
        laboratoryDTO.setLocation("Building X");
        laboratoryDTO.setDescription("Electronics lab");
    }

    @Test
    void testGetAllLaboratories_Success() {
        LaboratoryDTO secondDTO = new LaboratoryDTO();
        secondDTO.setId(2L);
        secondDTO.setLabName("Lab B");
        secondDTO.setLocation("Building Y");

        when(laboratoryService.getAlLaboratories()).thenReturn(List.of(laboratoryDTO, secondDTO));

        ResponseEntity<List<LaboratoryDTO>> response = laboratoryController.getAllLaboratories();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
        assertEquals(1L, response.getBody().get(0).getId());
        assertEquals("Lab A", response.getBody().get(0).getLabName());
        assertEquals(2L, response.getBody().get(1).getId());
        assertEquals("Lab B", response.getBody().get(1).getLabName());
    }

    @Test
    void testGetAllLaboratories_Empty() {
        when(laboratoryService.getAlLaboratories()).thenReturn(List.of());

        ResponseEntity<List<LaboratoryDTO>> response = laboratoryController.getAllLaboratories();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testGetLaboratoryById_Success() {
        when(laboratoryService.getLaboratoryById(1L)).thenReturn(laboratoryDTO);

        ResponseEntity<LaboratoryDTO> response = laboratoryController.getLaboratoryById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Lab A", response.getBody().getLabName());
    }

    @Test
    void testGetLaboratoryById_NotFound() {
        when(laboratoryService.getLaboratoryById(1L)).thenThrow(LaboratoryNotFoundException.class);

        assertThrows(LaboratoryNotFoundException.class, () -> laboratoryController.getLaboratoryById(1L));
    }

    @Test
    void testCreateLaboratory_Success() {
        when(laboratoryService.createLaboratory(laboratoryDTO)).thenReturn(laboratoryDTO);

        ResponseEntity<LaboratoryDTO> response = laboratoryController.createLaboratory(laboratoryDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Lab A", response.getBody().getLabName());
    }

    @Test
    void testUpdateLaboratory_Success() {
        LaboratoryDTO updatedDTO = new LaboratoryDTO();
        updatedDTO.setId(1L);
        updatedDTO.setLabName("Updated Lab");
        updatedDTO.setLocation("Updated Location");

        when(laboratoryService.updateLaboratory(1L, updatedDTO)).thenReturn(updatedDTO);

        ResponseEntity<LaboratoryDTO> response = laboratoryController.updateLaboratory(1L, updatedDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Lab", response.getBody().getLabName());
    }

    @Test
    void testUpdateLaboratory_NotFound() {
        when(laboratoryService.updateLaboratory(1L, laboratoryDTO)).thenThrow(LaboratoryNotFoundException.class);

        assertThrows(LaboratoryNotFoundException.class, () -> laboratoryController.updateLaboratory(1L, laboratoryDTO));
    }

    @Test
    void testDeleteLaboratory_Success() {
        doNothing().when(laboratoryService).deleteLaboratory(1L);

        ResponseEntity<Void> response = laboratoryController.deleteLaboratory(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteLaboratory_NotFound() {
        doThrow(LaboratoryNotFoundException.class).when(laboratoryService).deleteLaboratory(1L);

        assertThrows(LaboratoryNotFoundException.class, () -> laboratoryController.deleteLaboratory(1L));
    }
}
