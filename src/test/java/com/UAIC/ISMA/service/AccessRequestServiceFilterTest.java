package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dto.AccessRequestDTO;
import com.UAIC.ISMA.repository.AccessRequestRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class AccessRequestServiceFilterTest {

    private AccessRequestRepository repository;
    private AccessRequestService service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(AccessRequestRepository.class);
        service = new AccessRequestService(repository, null, null, null);
    }

    @Test
    void testFilterRequests() {
        Pageable pageable = PageRequest.of(0, 10);
        AccessRequestDTO dto = new AccessRequestDTO();
        dto.setId(1L);
        Page<AccessRequestDTO> page = new PageImpl<>(List.of(dto));

        when(repository.filterAccessRequests(null, null, null, pageable)).thenReturn(page);

        Page<AccessRequestDTO> result = service.filterRequests(null, null, null, pageable);

        assertEquals(1, result.getTotalElements());
        verify(repository, times(1)).filterAccessRequests(null, null, null, pageable);
    }
}