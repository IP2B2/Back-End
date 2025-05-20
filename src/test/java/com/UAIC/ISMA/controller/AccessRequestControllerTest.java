
package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.config.UserDetailsImpl;
import com.UAIC.ISMA.dto.AccessRequestDTO;
import com.UAIC.ISMA.entity.Role;
import com.UAIC.ISMA.entity.User;
import com.UAIC.ISMA.entity.enums.RoleName;
import com.UAIC.ISMA.exception.AccessRequestNotFoundException;
import com.UAIC.ISMA.service.AccessRequestService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

class AccessRequestControllerTest {

    @Mock
    private AccessRequestService accessRequestService;

    @InjectMocks
    private AccessRequestController accessRequestController;

    private AccessRequestDTO accessRequestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        accessRequestDTO = new AccessRequestDTO();
        accessRequestDTO.setId(1L);
        accessRequestDTO.setRequestDate(LocalDateTime.now());
        accessRequestDTO.setProposalFile("proposal.pdf");
        accessRequestDTO.setUserId(10L);
        accessRequestDTO.setEquipmentId(20L);
    }

    @Test
    void testGetAllAccessRequests_Success() {
        when(accessRequestService.findAll()).thenReturn(List.of(accessRequestDTO));

        ResponseEntity<List<AccessRequestDTO>> response = accessRequestController.getAllAccessRequests();

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetAccessRequestById_Success() {
        when(accessRequestService.findById(1L)).thenReturn(accessRequestDTO);

        ResponseEntity<AccessRequestDTO> response = accessRequestController.getAccessRequestById(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accessRequestDTO.getId(), response.getBody().getId());
    }

    @Test
    void testGetAccessRequestById_NotFound() {
        when(accessRequestService.findById(1L)).thenThrow(AccessRequestNotFoundException.class);

        assertThrows(AccessRequestNotFoundException.class, () -> accessRequestController.getAccessRequestById(1L));
    }

    @Test
    void testCreateAccessRequest_Success() {
        when(accessRequestService.create(accessRequestDTO)).thenReturn(accessRequestDTO);

        ResponseEntity<AccessRequestDTO> response = accessRequestController.createAccessRequest(accessRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.CREATED, response.getStatusCode()); // <- AICI modifici
        assertEquals(accessRequestDTO.getId(), response.getBody().getId());
    }

    @Test
    void testUpdateAccessRequest_Success() {
        when(accessRequestService.update(1L, accessRequestDTO)).thenReturn(accessRequestDTO);

        ResponseEntity<AccessRequestDTO> response = accessRequestController.updateAccessRequest(1L, accessRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(accessRequestDTO.getId(), response.getBody().getId());
    }

    @Test
    void testUpdatePartialAccessRequest_Success() {
        when(accessRequestService.updatePartial(eq(1L), anyMap())).thenReturn(accessRequestDTO);

        ResponseEntity<AccessRequestDTO> response = accessRequestController.updatePartialAccessRequest(1L, Map.of("status", "APPROVED"));

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteAccessRequest_Success() {
        doNothing().when(accessRequestService).delete(1L);

        ResponseEntity<Void> response = accessRequestController.deleteAccessRequest(1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteAccessRequest_NotFound() {
        doThrow(AccessRequestNotFoundException.class).when(accessRequestService).delete(1L);

        assertThrows(AccessRequestNotFoundException.class, () -> accessRequestController.deleteAccessRequest(1L));
    }
    @Test
    void testFilterAccessRequests_Success() {
        AccessRequestDTO dto = new AccessRequestDTO();
        dto.setId(1L);
        dto.setUserId(10L);
        dto.setEquipmentId(20L);
        dto.setProposalFile("proposal.pdf");
        dto.setRequestDate(LocalDateTime.now());

        Page<AccessRequestDTO> page = new PageImpl<>(List.of(dto));
        when(accessRequestService.filterRequests(any(), any(), any(), any())).thenReturn(page);

        User mockUser = new User();
        mockUser.setId(10L);
        mockUser.setRole(new Role(RoleName.ADMIN));

        UserDetailsImpl mockUserDetails = new UserDetailsImpl(mockUser);

        ResponseEntity<Page<AccessRequestDTO>> response = accessRequestController.filterAccessRequests(
                null, "type", 10L, 0, 10, mockUserDetails
        );

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getContent().size());
        assertEquals(1L, response.getBody().getContent().get(0).getId());
    }



}
