package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dao.RequestApproval;
import com.UAIC.ISMA.dao.enums.ApprovalStatus;
import com.UAIC.ISMA.dto.RequestApprovalDTO;
import com.UAIC.ISMA.exception.RequestApprovalNotFoundException;
import com.UAIC.ISMA.repository.AccessRequestRepository;
import com.UAIC.ISMA.repository.RequestApprovalRepository;
import com.UAIC.ISMA.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RequestApprovalServiceTest {

    @Mock
    private RequestApprovalRepository requestApprovalRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AccessRequestRepository accessRequestRepository;

    @InjectMocks
    private RequestApprovalService requestApprovalService;

    private RequestApproval requestApproval;
    private RequestApprovalDTO requestApprovalDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestApproval = new RequestApproval();
        requestApproval.setId(1L);
        requestApproval.setApprovalStatus(ApprovalStatus.NEEDS_MORE_INFO);
        requestApproval.setApprovalDate(LocalDateTime.parse("2025-05-08T21:48:13.828602"));
        requestApproval.setComments("Test comment");

        requestApprovalDTO = new RequestApprovalDTO();
        requestApprovalDTO.setId(1L);
        requestApprovalDTO.setApprovalStatus(ApprovalStatus.NEEDS_MORE_INFO.toString());
        requestApprovalDTO.setApprovalDate(LocalDateTime.parse("2025-05-08T21:48:13.828602"));
        requestApprovalDTO.setComments("Test comment");
    }

    @Test
    void testFindById_Success() {
        when(requestApprovalRepository.findById(1L)).thenReturn(Optional.of(requestApproval));

        RequestApprovalDTO result = requestApprovalService.findById(1L);

        assertNotNull(result);
        assertEquals(requestApproval.getId(), result.getId());
        assertEquals(requestApproval.getApprovalStatus().toString(), result.getApprovalStatus());
        assertEquals(requestApproval.getApprovalDate(), result.getApprovalDate());
        assertEquals(requestApproval.getComments(), result.getComments());
    }

    @Test
    void testFindById_RequestApprovalNotFound() {
        when(requestApprovalRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RequestApprovalNotFoundException.class, () -> requestApprovalService.findById(1L));
    }

    @Test
    void testCreate_Success() {
        when(requestApprovalRepository.save(any(RequestApproval.class))).thenReturn(requestApproval);

        RequestApprovalDTO result = requestApprovalService.create(requestApprovalDTO);

        assertNotNull(result);
        assertEquals(requestApproval.getId(), result.getId());
        assertEquals(requestApproval.getApprovalStatus().toString(), result.getApprovalStatus());
        assertEquals(requestApproval.getApprovalDate(), result.getApprovalDate());
        assertEquals(requestApproval.getComments(), result.getComments());
    }

    @Test
    void testCreate_Fail_NullRequestApprovalDTO() {
        assertThrows(NullPointerException.class, () -> requestApprovalService.create(null));
    }

    @Test
    void testUpdate_Success() {
        when(requestApprovalRepository.findById(1L)).thenReturn(Optional.of(requestApproval));
        when(requestApprovalRepository.save(any(RequestApproval.class))).thenReturn(requestApproval);

        requestApprovalDTO.setApprovalStatus(ApprovalStatus.APPROVED.toString());
        requestApprovalDTO.setApprovalDate(LocalDateTime.parse("2025-05-08T21:48:13.828602"));
        requestApprovalDTO.setComments("Updated comment");
        RequestApprovalDTO result = requestApprovalService.update(1L, requestApprovalDTO);

        assertNotNull(result);
        assertEquals("APPROVED", result.getApprovalStatus());
        assertEquals(LocalDateTime.parse("2025-05-08T21:48:13.828602"), result.getApprovalDate());
        assertEquals("Updated comment", result.getComments());
    }

    @Test
    void testUpdate_Fail_RequestApprovalNotFound() {
        when(requestApprovalRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RequestApprovalNotFoundException.class, () -> requestApprovalService.update(1L, requestApprovalDTO));
    }

    @Test
    void testUpdate_Fail_NullRequestApprovalDTO() {
        when(requestApprovalRepository.findById(1L)).thenReturn(Optional.of(requestApproval));

        assertThrows(NullPointerException.class, () -> requestApprovalService.update(1L, requestApprovalDTO));
    }

    @Test
    void testDelete_Success() {
        when(requestApprovalRepository.findById(1L)).thenReturn(Optional.of(requestApproval));

        requestApprovalService.delete(1L);

        verify(requestApprovalRepository, times(1)).delete(requestApproval);
    }

    @Test
    void testDelete_Fail_RequestApprovalNotFound() {
        when(requestApprovalRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RequestApprovalNotFoundException.class, () -> requestApprovalService.delete(1L));
    }

    @Test
    void testFindAll_Success() {
        RequestApproval requestApproval2 = new RequestApproval();
        requestApproval2.setId(2L);
        requestApproval2.setApprovalStatus(ApprovalStatus.APPROVED);
        requestApproval2.setApprovalDate(LocalDateTime.parse("2025-05-08T21:48:13.828602"));
        requestApproval2.setComments("Test comment 2");

        when(requestApprovalRepository.findAll()).thenReturn(List.of(requestApproval, requestApproval2));

        List<RequestApprovalDTO> result = requestApprovalService.findAll(1L, 1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(requestApproval.getId(), result.get(0).getId());
        assertEquals(requestApproval2.getId(), result.get(1).getId());
    }

    @Test
    void testFindAll_Fail_EmptyList() {
        when(requestApprovalRepository.findAll()).thenReturn(List.of());

        List<RequestApprovalDTO> result = requestApprovalService.findAll(1L, 1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}
