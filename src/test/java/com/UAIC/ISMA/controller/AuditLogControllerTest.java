package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.AuditLogDTO;
import com.UAIC.ISMA.exception.AuditLogNotFoundException;
import com.UAIC.ISMA.exception.UserNotFoundException;
import com.UAIC.ISMA.service.AuditLogService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuditLogControllerTest {

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private AuditLogController auditLogController;

    private MockMvc mockMvc;
    private AuditLogDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(auditLogController).build();

        dto = new AuditLogDTO();
        dto.setId(1L);
        dto.setAction("CREATE");
        dto.setDetails("Test action");
        dto.setTimestamp(LocalDateTime.of(2025, 5, 10, 10, 0));
        dto.setUserId(1L);
    }

    @Test
    void testFindById_Success() throws Exception {
        when(auditLogService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/audit-logs/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.action").value(dto.getAction()))
                .andExpect(jsonPath("$.details").value(dto.getDetails()));
    }

    @Test
    void testFindById_NotFound() throws Exception {
        when(auditLogService.findById(1L)).thenThrow(new AuditLogNotFoundException(1L));

        mockMvc.perform(get("/audit-logs/{id}", 1L))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreate_Success() throws Exception {
        when(auditLogService.create(any())).thenReturn(dto);

        mockMvc.perform(post("/audit-logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"action\":\"CREATE\",\"details\":\"Test action\",\"timestamp\":\"2025-05-10T10:00:00\",\"userId\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.action").value(dto.getAction()));
    }

    @Test
    void testCreate_InvalidUser() throws Exception {
        // Arrange
        when(auditLogService.create(any())).thenThrow(new UserNotFoundException(1L));

        // JSON valid pentru body
        String jsonContent = """
        {
            "id": 1,
            "action": "CREATE",
            "details": "Test action",
            "timestamp": "2025-05-10T10:00:00",
            "userId": 1
        }
    """;

        // Act & Assert
        mockMvc.perform(post("/audit-logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonContent))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("User not found with ID: 1"));
    }

    @Test
    void testUpdate_Success() throws Exception {
        dto.setAction("UPDATE");
        dto.setDetails("Updated action");

        when(auditLogService.update(eq(1L), any())).thenReturn(dto);

        mockMvc.perform(put("/audit-logs/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"id\":1,\"action\":\"UPDATE\",\"details\":\"Updated action\",\"timestamp\":\"2025-05-10T10:00:00\",\"userId\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.action").value("UPDATE"))
                .andExpect(jsonPath("$.details").value("Updated action"));
    }

    @Test
    void testDelete_Success() throws Exception {
        doNothing().when(auditLogService).delete(1L);

        mockMvc.perform(delete("/audit-logs/{id}", 1L))
                .andExpect(status().isNoContent());
    }

    @Test
    void testDelete_NotFound() throws Exception {
        doThrow(new AuditLogNotFoundException(1L)).when(auditLogService).delete(1L);

        mockMvc.perform(delete("/audit-logs/{id}", 1L))
                .andExpect(status().isNotFound());
    }
}