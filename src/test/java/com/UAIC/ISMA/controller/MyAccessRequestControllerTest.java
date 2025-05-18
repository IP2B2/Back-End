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
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;

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

        when(userDetails.getUsername()).thenReturn("testuser");
    }

    @Test
    void testGetMyAccessRequests_returnsFilteredList() {
        when(userService.findIdByUsername("testuser")).thenReturn(10L);
        when(accessRequestService.findByUserWithFilters(eq(10L), any(), any(), anyInt(), anyInt()))
                .thenReturn(List.of(dto));

        ResponseEntity<List<AccessRequestDTO>> response = controller.getMyAccessRequests(
                userDetails, null, null, 0, 10);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(dto.getId(), response.getBody().get(0).getId());
    }

    @Test
    void testGetMyAccessRequests_userNotFound() {
        when(userService.findIdByUsername("testuser"))
                .thenThrow(new UserNotFoundException("User not found"));

        assertThrows(UserNotFoundException.class, () ->
                controller.getMyAccessRequests(userDetails, null, null, 0, 10));
    }
}
