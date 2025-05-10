package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dao.AuditLog;
import com.UAIC.ISMA.dao.User;
import com.UAIC.ISMA.dto.AuditLogDTO;
import com.UAIC.ISMA.exception.AuditLogNotFoundException;
import com.UAIC.ISMA.mapper.AuditLogMapper;
import com.UAIC.ISMA.repository.AuditLogRepository;
import com.UAIC.ISMA.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuditLogServiceTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuditLogMapper auditLogMapper;

    @InjectMocks
    private AuditLogService auditLogService;

    private AuditLog auditLog;
    private AuditLogDTO dto;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);
        user.setUsername("testuser");

        auditLog = new AuditLog();
        auditLog.setId(1L);
        auditLog.setAction("CREATE");
        auditLog.setDetails("Test action");
        auditLog.setTimestamp(LocalDateTime.now());
        auditLog.setUser(user);

        dto = new AuditLogDTO();
        dto.setId(1L);
        dto.setAction("CREATE");
        dto.setDetails("Test action");
        dto.setTimestamp(LocalDateTime.now());
        dto.setUserId(1L);
    }

    @Test
    void testFindById_Success() {
        when(auditLogRepository.findById(1L)).thenReturn(Optional.of(auditLog));
        when(auditLogMapper.toDto(auditLog)).thenReturn(dto);

        AuditLogDTO result = auditLogService.findById(1L);

        assertNotNull(result);
        assertEquals(dto.getId(), result.getId());
    }

    @Test
    void testFindById_NotFound() {
        when(auditLogRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AuditLogNotFoundException.class, () -> auditLogService.findById(1L));
    }

    @Test
    void testCreate_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(auditLogMapper.toEntity(dto)).thenReturn(auditLog);
        when(auditLogRepository.save(auditLog)).thenReturn(auditLog);
        when(auditLogMapper.toDto(auditLog)).thenReturn(dto);

        AuditLogDTO result = auditLogService.create(dto);

        assertNotNull(result);
        assertEquals(dto.getAction(), result.getAction());
    }

    @Test
    void testCreate_UserNotFound() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> auditLogService.create(dto));
    }

    @Test
    void testUpdate_Success() {
        when(auditLogRepository.findById(1L)).thenReturn(Optional.of(auditLog));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(auditLogMapper.toEntity(dto)).thenReturn(auditLog);
        when(auditLogRepository.save(auditLog)).thenReturn(auditLog);
        when(auditLogMapper.toDto(auditLog)).thenReturn(dto);

        AuditLogDTO result = auditLogService.update(1L, dto);

        assertNotNull(result);
        assertEquals(dto.getDetails(), result.getDetails());
    }

    @Test
    void testUpdate_NotFound() {
        when(auditLogRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(AuditLogNotFoundException.class, () -> auditLogService.update(1L, dto));
    }

    @Test
    void testDelete_Success() {
        when(auditLogRepository.findById(1L)).thenReturn(Optional.of(auditLog));
        auditLogService.delete(1L);
        verify(auditLogRepository, times(1)).delete(auditLog);
    }

    @Test
    void testDelete_NotFound() {
        when(auditLogRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(AuditLogNotFoundException.class, () -> auditLogService.delete(1L));
    }
}
