package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dao.AuditLog;
import com.UAIC.ISMA.dao.User;
import com.UAIC.ISMA.dto.AuditLogDTO;
import com.UAIC.ISMA.exception.AuditLogNotFoundException;
import com.UAIC.ISMA.exception.EntityNotFoundException;
import com.UAIC.ISMA.mapper.AuditLogMapper;
import com.UAIC.ISMA.repository.AuditLogRepository;
import com.UAIC.ISMA.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AuditLogServiceTest {

    @InjectMocks
    private AuditLogService auditLogService;

    @Mock
    private AuditLogRepository auditLogRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private AuditLogMapper auditLogMapper;

    private AuditLogDTO dto;
    private AuditLog entity;
    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = new User();
        user.setId(1L);

        dto = new AuditLogDTO();
        dto.setId(1L);
        dto.setAction("CREATE");
        dto.setDetails("Test log");
        dto.setTimestamp(LocalDateTime.of(2025, 5, 11, 10, 0));
        dto.setUserId(1L);

        entity = new AuditLog();
        entity.setId(1L);
        entity.setAction("CREATE");
        entity.setDetails("Test log");
        entity.setTimestamp(dto.getTimestamp());
        entity.setUser(user);
    }

    @Test
    void testCreate_Success() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(auditLogMapper.toEntity(dto, user)).thenReturn(entity);
        when(auditLogRepository.save(entity)).thenReturn(entity);
        when(auditLogMapper.toDto(entity)).thenReturn(dto);

        AuditLogDTO result = auditLogService.create(dto);

        assertNotNull(result);
        assertEquals("CREATE", result.getAction());
    }

    @Test
    void testRead_Success() {
        when(auditLogRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(auditLogMapper.toDto(entity)).thenReturn(dto);

        AuditLogDTO result = auditLogService.findById(1L)
                ;

        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void testRead_NotFound() {
        when(auditLogRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(AuditLogNotFoundException.class, () -> auditLogService.findById(1L)
        );
    }

    @Test
    void testFindAll() {
        when(auditLogRepository.findAll()).thenReturn(List.of(entity));
        when(auditLogMapper.toDto(entity)).thenReturn(dto);

        List<AuditLogDTO> result = auditLogService.findAll();

        assertEquals(1, result.size());
    }

    @Test
    void testUpdate_Success() {
        AuditLogDTO updatedDto = new AuditLogDTO();
        updatedDto.setId(1L);
        updatedDto.setAction("UPDATE");
        updatedDto.setDetails("Modified");
        updatedDto.setTimestamp(dto.getTimestamp());
        updatedDto.setUserId(1L);

        when(auditLogRepository.findById(1L)).thenReturn(Optional.of(entity));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(auditLogRepository.save(entity)).thenReturn(entity);
        when(auditLogMapper.toDto(entity)).thenReturn(updatedDto);

        AuditLogDTO result = auditLogService.update(1L, updatedDto);

        assertEquals("UPDATE", result.getAction());
        assertEquals("Modified", result.getDetails());
    }

    @Test
    void testDelete_Success() {
        // Arrange
        AuditLog entity = new AuditLog();
        entity.setId(1L);

        when(auditLogRepository.findById(1L)).thenReturn(Optional.of(entity));
        doNothing().when(auditLogRepository).delete(entity);

        // Act
        auditLogService.delete(1L);

        // Assert
        verify(auditLogRepository, times(1)).findById(1L);
        verify(auditLogRepository, times(1)).delete(entity);
    }

    @Test
    void testDelete_NotFound() {
        when(auditLogRepository.existsById(1L)).thenReturn(false);

        assertThrows(AuditLogNotFoundException.class, () -> auditLogService.delete(1L));
    }
}
