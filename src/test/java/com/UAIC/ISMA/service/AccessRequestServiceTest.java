package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dto.AccessRequestDTO;
import com.UAIC.ISMA.entity.AccessRequest;
import com.UAIC.ISMA.entity.Equipment;
import com.UAIC.ISMA.entity.User;
import com.UAIC.ISMA.dto.AccessRequestDTO;
import com.UAIC.ISMA.entity.enums.RequestStatus;
import com.UAIC.ISMA.entity.enums.RequestType;
import com.UAIC.ISMA.exception.EntityNotFoundException;
import com.UAIC.ISMA.repository.AccessRequestRepository;
import com.UAIC.ISMA.repository.EquipmentRepository;
import com.UAIC.ISMA.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AccessRequestServiceTest {

    @Mock private AccessRequestRepository accessRequestRepository;
    @Mock private UserRepository userRepository;
    @Mock private EquipmentRepository equipmentRepository;

    @InjectMocks
    private AccessRequestService accessRequestService;

    private AccessRequest accessRequest;
    private AccessRequestDTO dto;
    private User user;
    private Equipment equipment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        // Setup entități
        user = new User();
        user.setId(1L);

        equipment = new Equipment();
        equipment.setId(1L);

        accessRequest = new AccessRequest();
        accessRequest.setId(1L);
        accessRequest.setUser(user);
        accessRequest.setEquipment(equipment);
        accessRequest.setRequestDate(LocalDateTime.now());
        accessRequest.setStatus(RequestStatus.PENDING);
        accessRequest.setRequestType(RequestType.VIRTUAL);
        accessRequest.setProposalFile("file.pdf");

        dto = new AccessRequestDTO();
        dto.setId(1L);
        dto.setUserId(1L);
        dto.setEquipmentId(1L);
        dto.setRequestDate(accessRequest.getRequestDate());
        dto.setStatus(RequestStatus.PENDING);
        dto.setRequestType(RequestType.VIRTUAL);
        dto.setProposalFile("file.pdf");

        // 🔐 Mock SecurityContext cu UserDetails
        org.springframework.security.core.Authentication authentication = mock(org.springframework.security.core.Authentication.class);
        org.springframework.security.core.userdetails.User mockUserDetails =
                new org.springframework.security.core.userdetails.User("username", "password", Collections.emptyList());
        when(authentication.getPrincipal()).thenReturn(mockUserDetails);

        org.springframework.security.core.context.SecurityContext securityContext = mock(org.springframework.security.core.context.SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        org.springframework.security.core.context.SecurityContextHolder.setContext(securityContext);

        // ✅ Lipsa mock aici cauza eroarea ta
        when(userRepository.findByUsername("username")).thenReturn(Optional.of(user));
    }






    @Test
    void shouldReturnDTO_whenFindByIdSuccess() {
        when(accessRequestRepository.findById(1L)).thenReturn(Optional.of(accessRequest));

        AccessRequestDTO result = accessRequestService.findById(1L);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(RequestStatus.PENDING, result.getStatus());
    }

    @Test
    void shouldThrowException_whenFindByIdFails() {
        when(accessRequestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> accessRequestService.findById(1L));
    }

    @Test
    void shouldCreateAccessRequest_whenValidInput() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));
        when(accessRequestRepository.save(any())).thenReturn(accessRequest);

        AccessRequestDTO result = accessRequestService.create(dto);

        assertNotNull(result);
        assertEquals(1L, result.getUserId());
        assertEquals(1L, result.getEquipmentId());
    }

    @Test
    void shouldThrow_whenUserNotFoundOnCreate() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> accessRequestService.create(dto));
    }

    @Test
    void shouldUpdateAccessRequest_whenValidInput() {
        when(accessRequestRepository.findById(1L)).thenReturn(Optional.of(accessRequest));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));
        when(accessRequestRepository.save(any())).thenReturn(accessRequest);

        AccessRequestDTO result = accessRequestService.update(1L, dto);

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void shouldThrow_whenUpdateNotFound() {
        when(accessRequestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> accessRequestService.update(1L, dto));
    }

    @Test
    void shouldDeleteAccessRequest_whenExists() {
        when(accessRequestRepository.findById(1L)).thenReturn(Optional.of(accessRequest));

        accessRequestService.delete(1L);

        verify(accessRequestRepository, times(1)).delete(accessRequest);
    }

    @Test
    void shouldReturnAllAccessRequests() {
        when(accessRequestRepository.findAll()).thenReturn(List.of(accessRequest));

        List<AccessRequestDTO> result = accessRequestService.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void shouldPartiallyUpdateAccessRequest_statusOnly() {
        when(accessRequestRepository.findById(1L)).thenReturn(Optional.of(accessRequest));
        when(accessRequestRepository.save(any())).thenReturn(accessRequest);

        Map<String, Object> updates = Map.of("status", "APPROVED");
        AccessRequestDTO result = accessRequestService.updatePartial(1L, updates);

        assertNotNull(result);
    }

}
