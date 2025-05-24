package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.AccessRequestDTO;
import com.UAIC.ISMA.exception.UserNotFoundException;
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

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class MyAccessRequestControllerTest {

    @Mock
    private AccessRequestService accessRequestService;

    @Mock
    private UserService userService;

    @Mock
    private UserDetails userDetails;

    @InjectMocks
    private MyAccessRequestController controller;

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
    void shouldReturnFilteredAccessRequestsForAuthenticatedUser() {
        when(userService.findIdByUsername("testuser")).thenReturn(10L);
        when(accessRequestService.findByUserWithFilters(eq(10L), any(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(dto));

        ResponseEntity<?> response = controller.getMyAccessRequests(userDetails, null, null, 0, 10);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        @SuppressWarnings("unchecked")
        List<AccessRequestDTO> body = (List<AccessRequestDTO>) response.getBody();

        assertNotNull(body);
        assertEquals(1, body.size());
        assertEquals(dto.getId(), body.get(0).getId());
    }

    @Test
    void shouldHandleUserNotFoundGracefully() {
        when(userService.findIdByUsername("testuser"))
                .thenThrow(new UserNotFoundException("User not found"));

        ResponseEntity<?> response = controller.getMyAccessRequests(userDetails, null, null, 0, 10);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("User not found.", response.getBody());
    }
}
