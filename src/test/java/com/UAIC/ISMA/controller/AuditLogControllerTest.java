package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.AuditLogDTO;
import com.UAIC.ISMA.exception.AuditLogNotFoundException;
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
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AuditLogControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuditLogService auditLogService;

    @InjectMocks
    private AuditLogController auditLogController;

    private AuditLogDTO dto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(auditLogController).build();

        dto = new AuditLogDTO();
        dto.setId(1L);
        dto.setAction("CREATE");
        dto.setDetails("Test");
        dto.setTimestamp(LocalDateTime.of(2025, 5, 11, 10, 0));
        dto.setUserId(1L);
    }

    @Test
    void testFindById_Success() throws Exception {
        when(auditLogService.findById(1L)).thenReturn(dto);

        mockMvc.perform(get("/api/audit-logs/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(dto.getId()))
                .andExpect(jsonPath("$.action").value(dto.getAction()));
    }

    @Test
    void testFindById_NotFound() throws Exception {
        when(auditLogService.findById(1L)).thenThrow(new AuditLogNotFoundException(1L));

        mockMvc.perform(get("/api/audit-logs/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testCreate_Success() throws Exception {
        when(auditLogService.create(any())).thenReturn(dto);

        String json = """
        {
            "id": 1,
            "action": "CREATE",
            "details": "Test",
            "timestamp": "2025-05-11T10:00:00",
            "userId": 1
        }
        """;

        mockMvc.perform(post("/api/audit-logs")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.action").value("CREATE"));
    }

    @Test
    void testFindAll() throws Exception {
        when(auditLogService.findAll()).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/audit-logs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    @Test
    void testDelete() throws Exception {
        doNothing().when(auditLogService).delete(1L);

        mockMvc.perform(delete("/api/audit-logs/1"))
                .andExpect(status().isNoContent());
    }
    @Test
    void testSearchByAction() throws Exception {
        when(auditLogService.searchByAction("CREATE")).thenReturn(List.of(dto));

        mockMvc.perform(get("/api/audit-logs/search?keyword=CREATE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1))
                .andExpect(jsonPath("$[0].action").value("CREATE"));
    }

}