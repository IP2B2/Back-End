package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.config.UserDetailsImpl;
import com.UAIC.ISMA.entity.Role;
import com.UAIC.ISMA.entity.User;
import com.UAIC.ISMA.entity.enums.RoleName;
import com.UAIC.ISMA.dto.AccessRequestDTO;
import com.UAIC.ISMA.service.AccessRequestService;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class AccessRequestControllerFilterTest {

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

    private UserDetailsImpl mockPrincipal(RoleName roleName, Long userId) {
        User user = new User();
        user.setId(userId);
        Role role = new Role();
        role.setRoleName(roleName);
        user.setRole(role);
        return new UserDetailsImpl(user);
    }

    @Test
    void testFilterAccessRequests_asStudent() {
        UserDetailsImpl student = mockPrincipal(RoleName.STUDENT, 5L);

        ResponseEntity<?> response = accessRequestController.filterAccessRequests(
                null, null, null, 0, 10, student
        );

        assertEquals(403, response.getStatusCodeValue());
        assertEquals("Only ADMIN and COORDONATOR can filter access requests.", response.getBody());
    }


    @Test
    void testFilterAccessRequests_asAdmin() {
        UserDetailsImpl admin = mockPrincipal(RoleName.ADMIN, 100L);
        Page<AccessRequestDTO> mockPage = new PageImpl<>(List.of(accessRequestDTO));

        when(accessRequestService.filterRequests(any(), any(), eq(null), any(Pageable.class)))
                .thenReturn(mockPage);

        ResponseEntity<?> response = accessRequestController.filterAccessRequests(
                null, null, null, 0, 10, admin
        );

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof Page);
        Page<?> page = (Page<?>) response.getBody();
        assertEquals(1, page.getTotalElements());

        verify(accessRequestService).filterRequests(null, null, null, PageRequest.of(0, 10));
    }


    @Test
    void testFilterAccessRequests_asResearcher_shouldBeForbidden() {
        UserDetailsImpl researcher = mockPrincipal(RoleName.RESEARCHER, 200L);

        ResponseEntity<?> response = accessRequestController.filterAccessRequests(
                null, null, null, 0, 10, researcher
        );

        assertEquals(403, response.getStatusCodeValue());
        assertEquals("Only ADMIN and COORDONATOR can filter access requests.", response.getBody());

        verifyNoInteractions(accessRequestService);
    }



    @Test
    void testFilterAccessRequests_asCoordinator() {
        UserDetailsImpl coordinator = mockPrincipal(RoleName.COORDONATOR, 300L);
        Page<AccessRequestDTO> mockPage = new PageImpl<>(List.of(accessRequestDTO));

        when(accessRequestService.filterRequests(any(), any(), eq(null), any(Pageable.class)))
                .thenReturn(mockPage);

        ResponseEntity<?> response = accessRequestController.filterAccessRequests(
                null, null, null, 0, 10, coordinator
        );

        assertEquals(200, response.getStatusCodeValue());
        assertNotNull(response.getBody());

        Page<?> page = (Page<?>) response.getBody();
        assertEquals(1, page.getTotalElements());

        verify(accessRequestService).filterRequests(null, null, null, PageRequest.of(0, 10));
    }

}
