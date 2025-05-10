package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.VirtualAccessDTO;
import com.UAIC.ISMA.exception.NotificationNotFoundException;
import com.UAIC.ISMA.exception.VirtualAccessNotFoundException;
import com.UAIC.ISMA.service.VirtualAccessService;
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

class VirtualAccessControllerTest {

    @Mock
    private VirtualAccessService virtualAccessService;

    @InjectMocks
    private VirtualAccessController controller;

    private VirtualAccessDTO vaDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        vaDTO = new VirtualAccessDTO();
        vaDTO.setId(1L);
        vaDTO.setUsername("user1");
        vaDTO.setPassword("pass1");
        vaDTO.setIssuedDate(LocalDateTime.now());
    }

    @Test
    void testGetAllVirtualAccess_Success() {
        when(virtualAccessService.findAll()).thenReturn(List.of(vaDTO));

        ResponseEntity<List<VirtualAccessDTO>> response = controller.getAllVirtualAccess();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
        assertEquals("user1", response.getBody().get(0).getUsername());
    }

    @Test
    void testGetAllVirtualAccess_EmptyList() {
        when(virtualAccessService.findAll()).thenReturn(List.of());

        ResponseEntity<List<VirtualAccessDTO>> response = controller.getAllVirtualAccess();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody().isEmpty(), "Expected empty list of VirtualAccessDTOs");
    }


    @Test
    void testGetVirtualAccessById_Success() {
        when(virtualAccessService.findById(1L)).thenReturn(vaDTO);

        ResponseEntity<VirtualAccessDTO> response = controller.getVirtualAccessById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("user1", response.getBody().getUsername());
    }

    @Test
    void testGetVirtualAccessById_VirtualAccessNotFound() {
        when(virtualAccessService.findById(1L)).thenThrow(VirtualAccessNotFoundException.class);

        assertThrows(VirtualAccessNotFoundException.class, () -> controller.getVirtualAccessById(1L));
    }

    @Test
    void testCreateVirtualAccess_Success() {
        when(virtualAccessService.create(vaDTO)).thenReturn(vaDTO);

        ResponseEntity<VirtualAccessDTO> response = controller.createVirtualAccess(vaDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("user1", response.getBody().getUsername());
    }

    @Test
    void testUpdateVirtualAccess_Success() {
        vaDTO.setUsername("updated");
        when(virtualAccessService.update(1L, vaDTO)).thenReturn(vaDTO);

        ResponseEntity<VirtualAccessDTO> response = controller.updateVirtualAccess(1L,vaDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("updated", response.getBody().getUsername());
    }

    @Test
    void testUpdateNotification_VirtualAccessNotFound() {
        when(virtualAccessService.update(1L, vaDTO)).thenThrow(VirtualAccessNotFoundException.class);

        assertThrows(VirtualAccessNotFoundException.class, () -> controller.updateVirtualAccess(1L, vaDTO));
    }

    @Test
    void testDeleteVirtualAccess() {
        doNothing().when(virtualAccessService).deleteById(1L);

        ResponseEntity<Void> response = controller.deleteVirtualAccess(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteNotification_NotificationNotFound() {
        doThrow(VirtualAccessNotFoundException.class).when(virtualAccessService).deleteById(1L);

        assertThrows(VirtualAccessNotFoundException.class, () -> controller.deleteVirtualAccess(1L));
    }
}

