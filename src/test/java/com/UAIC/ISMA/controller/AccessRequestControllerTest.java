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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccessRequestControllerTest {

    @Mock
    private AccessRequestService accessRequestService;

    @InjectMocks
    private AccessRequestController accessRequestController;

    private AccessRequestDTO accessRequestDTO;
    private UserDetailsImpl adminUserDetails;
    private UserDetailsImpl studentUserDetails;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        accessRequestDTO = new AccessRequestDTO();
        accessRequestDTO.setId(1L);
        accessRequestDTO.setRequestDate(LocalDateTime.now());
        accessRequestDTO.setProposalFile("proposal.pdf");
        accessRequestDTO.setUserId(10L);
        accessRequestDTO.setEquipmentId(20L);

        User adminUser = new User();
        adminUser.setId(10L);
        adminUser.setRole(new Role(RoleName.ADMIN));
        adminUserDetails = new UserDetailsImpl(adminUser);

        User studentUser = new User();
        studentUser.setId(20L);
        studentUser.setRole(new Role(RoleName.STUDENT));
        studentUserDetails = new UserDetailsImpl(studentUser);
    }

    @Test
    void testGetAllAccessRequests_Success() {
        when(accessRequestService.findAll()).thenReturn(List.of(accessRequestDTO));

        ResponseEntity<List<AccessRequestDTO>> response =
                accessRequestController.getAllAccessRequests(adminUserDetails);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    void testGetAllAccessRequests_ForbiddenForStudent() {
        ResponseEntity<List<AccessRequestDTO>> response =
                accessRequestController.getAllAccessRequests(studentUserDetails);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
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
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(accessRequestDTO.getId(), response.getBody().getId());
    }

    @Test
    void testUpdateAccessRequest_Success() {
        when(accessRequestService.update(1L, accessRequestDTO)).thenReturn(accessRequestDTO);

        ResponseEntity<AccessRequestDTO> response =
                accessRequestController.updateAccessRequest(adminUserDetails, 1L, accessRequestDTO);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateAccessRequest_ForbiddenForStudent() {
        ResponseEntity<AccessRequestDTO> response =
                accessRequestController.updateAccessRequest(studentUserDetails, 1L, accessRequestDTO);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testUpdatePartialAccessRequest_Success() {
        when(accessRequestService.updatePartial(eq(1L), anyMap())).thenReturn(accessRequestDTO);

        ResponseEntity<AccessRequestDTO> response =
                accessRequestController.updatePartialAccessRequest(adminUserDetails, 1L, Map.of("status", "APPROVED"));

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdatePartialAccessRequest_ForbiddenForStudent() {
        ResponseEntity<AccessRequestDTO> response =
                accessRequestController.updatePartialAccessRequest(studentUserDetails, 1L, Map.of("status", "APPROVED"));

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testDeleteAccessRequest_Success() {
        doNothing().when(accessRequestService).delete(1L);

        ResponseEntity<Void> response =
                accessRequestController.deleteAccessRequest(adminUserDetails, 1L);

        assertNotNull(response);
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    void testDeleteAccessRequest_ForbiddenForStudent() {
        ResponseEntity<Void> response =
                accessRequestController.deleteAccessRequest(studentUserDetails, 1L);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void testFilterAccessRequests_Success() {
        Page<AccessRequestDTO> page = new PageImpl<>(List.of(accessRequestDTO));
        when(accessRequestService.filterRequests(any(), any(), any(), any())).thenReturn(page);

        ResponseEntity<Page<AccessRequestDTO>> response =
                accessRequestController.filterAccessRequests(
                        null, "type", 10L, 0, 10, adminUserDetails);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().getContent().size());
    }

    @Test
    void testFilterAccessRequests_ForbiddenForStudent() {
        ResponseEntity<Page<AccessRequestDTO>> response =
                accessRequestController.filterAccessRequests(
                        null, "type", 10L, 0, 10, studentUserDetails);

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
