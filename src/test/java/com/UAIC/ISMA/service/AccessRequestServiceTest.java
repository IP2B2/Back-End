
package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dao.AccessRequest;
import com.UAIC.ISMA.dao.Equipment;
import com.UAIC.ISMA.dao.User;
import com.UAIC.ISMA.dto.AccessRequestDTO;
import com.UAIC.ISMA.exception.EntityNotFoundException;
import com.UAIC.ISMA.mapper.AccessRequestMapper;
import com.UAIC.ISMA.repository.AccessRequestRepository;
import com.UAIC.ISMA.repository.EquipmentRepository;
import com.UAIC.ISMA.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.UAIC.ISMA.dao.enums.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import java.time.LocalDate;

class AccessRequestServiceTest {

    @Mock
    private AccessRequestRepository accessRequestRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private EquipmentRepository equipmentRepository;

    @InjectMocks
    private AccessRequestService accessRequestService;

    private AccessRequest accessRequest;
    private AccessRequestDTO dto;
    private User user;
    private Equipment equipment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        equipment = new Equipment();
        equipment.setId(1L);

        accessRequest = new AccessRequest();
        accessRequest.setId(1L);
        accessRequest.setUser(user);
        accessRequest.setEquipment(equipment);

        dto = new AccessRequestDTO();
        dto.setId(1L);
        dto.setUserId(1L);
        dto.setEquipmentId(1L);
        dto.setRequestDate(LocalDateTime.now());
    }

    @Test
    void testFindById_Success() {
        when(accessRequestRepository.findById(1L)).thenReturn(Optional.of(accessRequest));

        AccessRequestDTO result = accessRequestService.findById(1L);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
    }

    @Test
    void testFindById_NotFound() {
        when(accessRequestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> accessRequestService.findById(1L));
    }

    @Test
    void testCreate_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));
        when(accessRequestRepository.save(any(AccessRequest.class))).thenReturn(accessRequest);

        AccessRequestDTO result = accessRequestService.create(dto);

        assertNotNull(result);
    }

    @Test
    void testCreate_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> accessRequestService.create(dto));
    }

    @Test
    void testUpdate_Success() {
        when(accessRequestRepository.findById(1L)).thenReturn(Optional.of(accessRequest));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));
        when(accessRequestRepository.save(any(AccessRequest.class))).thenReturn(accessRequest);

        AccessRequestDTO result = accessRequestService.update(1L, dto);

        assertNotNull(result);
    }

    @Test
    void testUpdate_NotFound() {
        when(accessRequestRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> accessRequestService.update(1L, dto));
    }

    @Test
    void testDelete_Success() {
        when(accessRequestRepository.findById(1L)).thenReturn(Optional.of(accessRequest));

        accessRequestService.delete(1L);

        verify(accessRequestRepository, times(1)).delete(accessRequest);
    }


    @Test
    void testFindAll_Success() {
        when(accessRequestRepository.findAll()).thenReturn(List.of(accessRequest));

        List<AccessRequestDTO> result = accessRequestService.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void testUpdatePartial_StatusOnly() {
        when(accessRequestRepository.findById(1L)).thenReturn(Optional.of(accessRequest));
        when(accessRequestRepository.save(any(AccessRequest.class))).thenReturn(accessRequest);

        Map<String, Object> updates = Map.of("status", "PENDING");
        AccessRequestDTO result = accessRequestService.updatePartial(1L, updates);

        assertNotNull(result);
    }

    @Test
    void testFindByUserWithFilters_Success() {
        Long userId = 1L;
        RequestStatus status = RequestStatus.APPROVED;
        LocalDate date = LocalDate.of(2025, 5, 14);
        int page = 0;
        int size = 10;

        PageRequest pageable = PageRequest.of(page, size);
        Page<AccessRequest> mockPage = new PageImpl<>(List.of(accessRequest));

        when(accessRequestRepository.findByUserWithFilters(userId, status, date, pageable))
                .thenReturn(mockPage);

        List<AccessRequestDTO> result = accessRequestService.findByUserWithFilters(userId, status, date, page, size);

        assertEquals(1, result.size());
        assertEquals(accessRequest.getId(), result.get(0).getId());
    }

}
