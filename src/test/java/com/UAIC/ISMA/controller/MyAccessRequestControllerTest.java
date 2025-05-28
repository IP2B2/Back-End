package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.AccessRequestDTO;
import com.UAIC.ISMA.entity.enums.RequestStatus;
import com.UAIC.ISMA.service.AccessRequestService;
import com.UAIC.ISMA.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class MyAccessRequestControllerTest {

    @Mock private AccessRequestService accessRequestService;
    @Mock private UserService userService;
    @Mock private UserDetails userDetails;

    @InjectMocks private MyAccessRequestController controller;

    private AccessRequestDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        dto = new AccessRequestDTO();
        dto.setId(1L);
        dto.setUserId(10L);
        dto.setEquipmentId(20L);
        dto.setRequestDate(LocalDateTime.now());
        dto.setProposalFile("proposal.pdf");

        when(userDetails.getUsername()).thenReturn("testuser");
    }

    @Test
    void shouldReturnAccessRequestsForAuthenticatedUser() {
        when(userService.findIdByUsername("testuser")).thenReturn(10L);
        when(accessRequestService.findByUserWithFilters(eq(10L), any(), any(), eq(0), eq(10)))
                .thenReturn(List.of(dto));

        ResponseEntity<?> response = controller.getMyAccessRequests(userDetails, null, null, 0, 10);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertTrue(response.getBody() instanceof List);
        List<?> body = (List<?>) response.getBody();
        assertEquals(1, body.size());
    }

    @Test
    void shouldReturnNotFoundIfNoAccessRequestsExist() {
        when(userService.findIdByUsername("testuser")).thenReturn(10L);
        when(accessRequestService.findByUserWithFilters(eq(10L), any(), any(), eq(0), eq(10)))
                .thenReturn(Collections.emptyList());

        ResponseEntity<?> response = controller.getMyAccessRequests(userDetails, null, null, 0, 10);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertTrue(response.getBody().toString().contains("No access requests"));
    }

    @Test
    void shouldReturnUnauthorizedIfUserDetailsIsNull() {
        ResponseEntity<?> response = controller.getMyAccessRequests(null, null, null, 0, 10);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertEquals("User not authenticated.", response.getBody());
    }

    @Test
    void shouldReturnAccessRequestsWithFilters() {
        when(userService.findIdByUsername("testuser")).thenReturn(10L);
        when(accessRequestService.findByUserWithFilters(eq(10L), eq(RequestStatus.APPROVED), any(), eq(0), eq(5)))
                .thenReturn(List.of(dto));

        ResponseEntity<?> response = controller.getMyAccessRequests(userDetails, RequestStatus.APPROVED, LocalDate.now(), 0, 5);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<?> body = (List<?>) response.getBody();
        assertEquals(1, body.size());
    }
}
