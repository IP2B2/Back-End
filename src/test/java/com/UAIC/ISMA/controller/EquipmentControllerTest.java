package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.entity.enums.AvailabilityStatus;
import com.UAIC.ISMA.dto.EquipmentDTO;
import com.UAIC.ISMA.exception.EquipmentNotFoundException;
import com.UAIC.ISMA.exception.InvalidInputException;
import com.UAIC.ISMA.service.EquipmentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EquipmentControllerTest {

    @Mock
    private EquipmentService equipmentService;

    @InjectMocks
    private EquipmentController equipmentController;

    private EquipmentDTO equipmentDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        equipmentDTO = new EquipmentDTO();
        equipmentDTO.setId(1L);
        equipmentDTO.setName("Oscilloscope");
        equipmentDTO.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        equipmentDTO.setLaboratoryId(10L);
    }

    @Test
    void testGetAllEquipments_Success() {
        EquipmentDTO secondDTO = new EquipmentDTO();
        secondDTO.setId(2L);
        secondDTO.setName("Spectometer");
        secondDTO.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);

        when(equipmentService.getAllEquipments(null)).thenReturn(List.of(equipmentDTO, secondDTO));

        ResponseEntity<List<EquipmentDTO>> response = equipmentController.getAllEquipment(null);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetAllEquipment_EmptyList() {
        when(equipmentService.getAllEquipments(null)).thenReturn(List.of());

        ResponseEntity<List<EquipmentDTO>> response = equipmentController.getAllEquipment(null);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty());
    }

    @Test
    void testGetEquipmentById_Success() {
        when(equipmentService.getEquipmentById(1L)).thenReturn(equipmentDTO);

        ResponseEntity<EquipmentDTO> response = equipmentController.getEquipmentById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Oscilloscope", response.getBody().getName());
    }

    @Test
    void testGetEquipmentById_NotFound() {
        when(equipmentService.getEquipmentById(1L)).thenThrow(EquipmentNotFoundException.class);

        assertThrows(EquipmentNotFoundException.class, () -> equipmentController.getEquipmentById(1L));
    }

    @Test
    void testCreateEquipment_Success() {
        when(equipmentService.createEquipment(equipmentDTO)).thenReturn(equipmentDTO);

        ResponseEntity<EquipmentDTO> response = equipmentController.createEquipment(equipmentDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("Oscilloscope", response.getBody().getName());
    }

    @Test
    void testUpdateEquipment_Success() {
        equipmentDTO.setName("Updated Name");

        when(equipmentService.updateEquipment( equipmentDTO, 1L)).thenReturn(equipmentDTO);

        ResponseEntity<EquipmentDTO> response = equipmentController.updateEquipment(1L, equipmentDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Updated Name", response.getBody().getName());
    }

    @Test
    void testUpdateEquipment_NotFound() {
        when(equipmentService.updateEquipment( equipmentDTO, 1L)).thenThrow(EquipmentNotFoundException.class);

        assertThrows(EquipmentNotFoundException.class, () -> equipmentController.updateEquipment(1L, equipmentDTO));
    }

    @Test
    void testDeleteEquipment_Success() {
        doNothing().when(equipmentService).deleteEquipment(1L);

        ResponseEntity<Void> response = equipmentController.deleteEquipment(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteEquipment_NotFound() {
        doThrow(EquipmentNotFoundException.class).when(equipmentService).deleteEquipment(1L);

        assertThrows(EquipmentNotFoundException.class, () -> equipmentController.deleteEquipment(1L));
    }

    @Test
    void testSearchEquipment_AllFilters_Success() {
        String name = "Oscilloscope";
        String status = "AVAILABLE";
        Long labId = 10L;
        Pageable pageable = PageRequest.of(0, 10);

        Page<EquipmentDTO> page = new PageImpl<>(List.of(equipmentDTO));

        when(equipmentService.searchEquipment(eq(name), eq(status), eq(labId), eq(pageable)))
                .thenReturn(page);

        ResponseEntity<?> response = equipmentController.searchEquipment(name, status, labId, pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Page<?> resultPage = (Page<?>) response.getBody();
        assertEquals(1, resultPage.getContent().size());

        EquipmentDTO dto = (EquipmentDTO) resultPage.getContent().get(0);
        assertEquals("Oscilloscope", dto.getName());
        assertEquals(AvailabilityStatus.AVAILABLE, dto.getAvailabilityStatus());
        assertEquals(10L, dto.getLaboratoryId());
    }

    @Test
    void testSearchEquipment_ByNameOnly_Success() {
        String name = "Oscilloscope";
        String status = null;
        Long labId = null;
        Pageable pageable = PageRequest.of(0, 10);

        Page<EquipmentDTO> page = new PageImpl<>(List.of(equipmentDTO));

        when(equipmentService.searchEquipment(eq(name), eq(status), eq(labId), eq(pageable)))
                .thenReturn(page);

        ResponseEntity<?> response = equipmentController.searchEquipment(name, status, labId, pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Page<?> resultPage = (Page<?>) response.getBody();
        assertEquals(1, resultPage.getContent().size());

        EquipmentDTO dto = (EquipmentDTO) resultPage.getContent().get(0);
        assertEquals("Oscilloscope", dto.getName());
        assertEquals(AvailabilityStatus.AVAILABLE, dto.getAvailabilityStatus());
        assertEquals(10L, dto.getLaboratoryId());
    }

    @Test
    void testSearchEquipment_ByStatusOnly_Success() {
        String name = null;
        String status = "AVAILABLE";
        Long labId = null;
        Pageable pageable = PageRequest.of(0, 10);

        Page<EquipmentDTO> page = new PageImpl<>(List.of(equipmentDTO));

        when(equipmentService.searchEquipment(eq(name), eq(status), eq(labId), eq(pageable)))
                .thenReturn(page);

        ResponseEntity<?> response = equipmentController.searchEquipment(name, status, labId, pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Page<?> resultPage = (Page<?>) response.getBody();
        assertEquals(1, resultPage.getContent().size());

        EquipmentDTO dto = (EquipmentDTO) resultPage.getContent().get(0);
        assertEquals("Oscilloscope", dto.getName());
        assertEquals(AvailabilityStatus.AVAILABLE, dto.getAvailabilityStatus());
        assertEquals(10L, dto.getLaboratoryId());
    }

    @Test
    void testSearchEquipment_ByLabIdOnly_Success() {
        String name = null;
        String status = null;
        Long labId = 10L;
        Pageable pageable = PageRequest.of(0, 10);

        Page<EquipmentDTO> page = new PageImpl<>(List.of(equipmentDTO));

        when(equipmentService.searchEquipment(eq(name), eq(status), eq(labId), eq(pageable)))
                .thenReturn(page);

        ResponseEntity<?> response = equipmentController.searchEquipment(name, status, labId, pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Page<?> resultPage = (Page<?>) response.getBody();
        assertEquals(1, resultPage.getContent().size());

        EquipmentDTO dto = (EquipmentDTO) resultPage.getContent().get(0);
        assertEquals("Oscilloscope", dto.getName());
        assertEquals(AvailabilityStatus.AVAILABLE, dto.getAvailabilityStatus());
        assertEquals(10L, dto.getLaboratoryId());

    }

    @Test
    void testSearchEquipment_NoFilters_Success() {
        String name = null;
        String status = null;
        Long labId = null;
        Pageable pageable = PageRequest.of(0, 10);

        Page<EquipmentDTO> page = new PageImpl<>(List.of(equipmentDTO));

        when(equipmentService.searchEquipment(eq(name), eq(status), eq(labId), eq(pageable)))
                .thenReturn(page);

        ResponseEntity<?> response = equipmentController.searchEquipment(name, status, labId, pageable);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        Page<?> resultPage = (Page<?>) response.getBody();
        assertEquals(1, resultPage.getContent().size());

        EquipmentDTO dto = (EquipmentDTO) resultPage.getContent().get(0);
        assertEquals("Oscilloscope", dto.getName());
        assertEquals(AvailabilityStatus.AVAILABLE, dto.getAvailabilityStatus());
        assertEquals(10L, dto.getLaboratoryId());
    }

    @Test
    void testSearchEquipment_InvalidStatus() {
        String invalidStatus = "INVALID";
        Pageable pageable = PageRequest.of(0, 10);

        when(equipmentService.searchEquipment(null, invalidStatus, null, pageable))
                .thenThrow(new InvalidInputException("Invalid availability status: " + invalidStatus));

        assertThrows(InvalidInputException.class, () ->
                equipmentController.searchEquipment(null, invalidStatus, null, pageable));
    }

}
