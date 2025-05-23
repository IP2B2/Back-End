package com.UAIC.ISMA.service;

import com.UAIC.ISMA.entity.AccessRequest;
import com.UAIC.ISMA.entity.RequestApproval;
import com.UAIC.ISMA.entity.User;
import com.UAIC.ISMA.entity.enums.ApprovalStatus;
import com.UAIC.ISMA.dto.RequestApprovalDTO;
import com.UAIC.ISMA.exception.RequestApprovalNotFoundException;
import com.UAIC.ISMA.exception.UserNotFoundException;
import com.UAIC.ISMA.repository.AccessRequestRepository;
import com.UAIC.ISMA.repository.NotificationRepository;
import com.UAIC.ISMA.repository.RequestApprovalRepository;
import com.UAIC.ISMA.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;

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

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private RequestApprovalService requestApprovalService;

    private RequestApproval requestApproval;
    private RequestApprovalDTO requestApprovalDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        requestApproval = new RequestApproval();
        requestApproval.setId(1L);
        requestApproval.setApprovalStatus(ApprovalStatus.PENDING);
        requestApproval.setApprovalDate(LocalDateTime.now());
        requestApproval.setComments("Test comment");
        AccessRequest accessRequest = new AccessRequest();
        accessRequest.setId(1L);
        requestApproval.setAccessRequest(accessRequest);
        User approver = new User();
        approver.setId(1L);
        requestApproval.setApprover(approver);

        requestApprovalDTO = new RequestApprovalDTO();
        requestApprovalDTO.setId(1L);
        requestApprovalDTO.setApprovalStatus(ApprovalStatus.PENDING.toString());
        requestApprovalDTO.setApprovalDate(LocalDateTime.now());
        requestApprovalDTO.setComments("Test comment");
        requestApprovalDTO.setAccessRequestId(1L);
        requestApprovalDTO.setApproverId(1L);
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
        AccessRequest accessRequest = new AccessRequest();
        accessRequest.setId(1L);
        when(accessRequestRepository.findById(1L)).thenReturn(Optional.of(accessRequest));
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
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("admin");
        var authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(userDetails, null);
        org.springframework.security.core.context.SecurityContext securityContext = mock(org.springframework.security.core.context.SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        org.springframework.security.core.context.SecurityContextHolder.setContext(securityContext);

        var adminRole = new com.UAIC.ISMA.entity.Role();
        adminRole.setRoleName(com.UAIC.ISMA.entity.enums.RoleName.ADMIN);
        var approver = new com.UAIC.ISMA.entity.User();
        approver.setId(100L);
        approver.setUsername("admin");
        approver.setRole(adminRole);

        var requestUser = new com.UAIC.ISMA.entity.User();
        requestUser.setId(200L);
        var accessRequest = new com.UAIC.ISMA.entity.AccessRequest();
        accessRequest.setId(1L);
        accessRequest.setUser(requestUser);

        requestApproval.setAccessRequest(accessRequest);

        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(approver));
        when(requestApprovalRepository.findById(1L)).thenReturn(Optional.of(requestApproval));
        when(requestApprovalRepository.save(any(RequestApproval.class))).thenAnswer(i -> i.getArgument(0));
        requestApprovalService.notificationRepository = notificationRepository;

        requestApprovalDTO.setApprovalStatus(ApprovalStatus.APPROVED.toString());
        requestApprovalDTO.setComments("Updated comment");
        RequestApprovalDTO result = requestApprovalService.update(1L, requestApprovalDTO);

        assertNotNull(result);
        assertEquals("APPROVED", result.getApprovalStatus());
        assertEquals("Updated comment", result.getComments());
        verify(notificationRepository, times(1)).save(any(com.UAIC.ISMA.entity.Notification.class));
    }

    @Test
    void testUpdate_Fail_RequestApprovalNotFound() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("admin");
        var authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(userDetails, null);
        org.springframework.security.core.context.SecurityContext securityContext = mock(org.springframework.security.core.context.SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        org.springframework.security.core.context.SecurityContextHolder.setContext(securityContext);

        var adminRole = new com.UAIC.ISMA.entity.Role();
        adminRole.setRoleName(com.UAIC.ISMA.entity.enums.RoleName.ADMIN);
        var approver = new com.UAIC.ISMA.entity.User();
        approver.setId(100L);
        approver.setUsername("admin");
        approver.setRole(adminRole);
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(approver));

        when(requestApprovalRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RequestApprovalNotFoundException.class, () -> requestApprovalService.update(1L, requestApprovalDTO));
    }

    @Test
    void testUpdate_Fail_NullApprovalStatus() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("admin");
        var authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(userDetails, null);
        org.springframework.security.core.context.SecurityContext securityContext = mock(org.springframework.security.core.context.SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        org.springframework.security.core.context.SecurityContextHolder.setContext(securityContext);

        when(requestApprovalRepository.findById(1L)).thenReturn(Optional.of(requestApproval));
        requestApprovalDTO.setApprovalStatus(null);

        assertThrows(com.UAIC.ISMA.exception.InvalidInputException.class, () -> requestApprovalService.update(1L, requestApprovalDTO));
    }

    @Test
    void testUpdate_Fail_UserNotFound() {
        UserDetails userDetails = mock(UserDetails.class);
        when(userDetails.getUsername()).thenReturn("admin");
        var authentication = new org.springframework.security.authentication.UsernamePasswordAuthenticationToken(userDetails, null);
        org.springframework.security.core.context.SecurityContext securityContext = mock(org.springframework.security.core.context.SecurityContext.class);
        when(securityContext.getAuthentication()).thenReturn(authentication);
        org.springframework.security.core.context.SecurityContextHolder.setContext(securityContext);

        when(userRepository.findByUsername("admin")).thenReturn(Optional.empty());
        when(requestApprovalRepository.findById(1L)).thenReturn(Optional.of(requestApproval));

        assertThrows(UserNotFoundException.class, () -> requestApprovalService.update(1L, requestApprovalDTO));
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
        requestApproval2.setApprovalStatus(ApprovalStatus.PENDING);
        requestApproval2.setApprovalDate(LocalDateTime.now());
        requestApproval2.setComments("Test comment 2");

        User approver = new User();
        approver.setId(1L);
        requestApproval2.setApprover(approver);

        AccessRequest accessRequest = new AccessRequest();
        accessRequest.setId(1L);
        requestApproval2.setAccessRequest(accessRequest);

        when(requestApprovalRepository.findByApprover_IdAndAccessRequest_Id(approver.getId(), accessRequest.getId()))
                .thenReturn(List.of(requestApproval, requestApproval2));

        List<RequestApprovalDTO> result = requestApprovalService.findAll(1L, 1L);

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(requestApproval.getId(), result.get(0).getId());
        assertEquals(requestApproval2.getId(), result.get(1).getId());
    }

    @Test
    void testFindAll_Fail_EmptyList() {
        when(requestApprovalRepository.findByApprover_IdAndAccessRequest_Id(1L, 1L)).thenReturn(List.of());

        List<RequestApprovalDTO> result = requestApprovalService.findAll(1L, 1L);

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }
}