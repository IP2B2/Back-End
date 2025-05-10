package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dao.VirtualAccess;
import com.UAIC.ISMA.dto.VirtualAccessDTO;
import com.UAIC.ISMA.exception.InvalidInputException;
import com.UAIC.ISMA.exception.VirtualAccessNotFoundException;
import com.UAIC.ISMA.repository.VirtualAccessRepository;
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

class VirtualAccessServiceTest {

    @Mock
    private VirtualAccessRepository virtualAccessRepository;

    @InjectMocks
    private VirtualAccessService virtualAccessService;

    private VirtualAccess virtualAccess;
    private VirtualAccessDTO virtualAccessDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        virtualAccess = new VirtualAccess("user0", "pass0", null);
        virtualAccess.setId(1L);

        virtualAccessDTO = new VirtualAccessDTO();
        virtualAccessDTO.setId(1L);
        virtualAccessDTO.setUsername("user1");
        virtualAccessDTO.setPassword("pass1");
        virtualAccessDTO.setIssuedDate(LocalDateTime.now());
    }

    @Test
    void testFindAll_Success() {
        VirtualAccess va2 = new VirtualAccess("user2", "pass2", null);
        va2.setId(2L);
        when(virtualAccessRepository.findAll()).thenReturn(List.of(virtualAccess, va2));

        List<VirtualAccessDTO> result = virtualAccessService.findAll();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(virtualAccess.getId(), result.get(0).getId());
        assertEquals(va2.getId(), result.get(1).getId());
    }

    @Test
    void testFindById_Success() {
        when(virtualAccessRepository.findById(1L)).thenReturn(Optional.of(virtualAccess));

        VirtualAccessDTO result = virtualAccessService.findById(1L);

        assertNotNull(result);
        assertEquals(virtualAccess.getUsername(), result.getUsername());
        assertEquals(virtualAccess.getPassword(), result.getPassword());
        assertEquals(virtualAccess.getIssuedDate(), result.getIssuedDate());
    }

    @Test
    void testFindById_VirtualAccessNotFound() {
        when(virtualAccessRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(VirtualAccessNotFoundException.class, () -> virtualAccessService.findById(1L));
    }

    @Test
    void testCreate_Success() {
        when(virtualAccessRepository.save(any(VirtualAccess.class))).thenReturn(virtualAccess);

        VirtualAccessDTO result = virtualAccessService.create(virtualAccessDTO);

        assertNotNull(result);
        assertEquals(virtualAccess.getId(), result.getId());
        assertEquals(virtualAccess.getUsername(), result.getUsername());
        assertEquals(virtualAccess.getPassword(), result.getPassword());
    }

    @Test
    void testCreate_Fail_NullVirtualAccessDTO() {
        assertThrows(NullPointerException.class, () -> virtualAccessService.create(null));
    }

    @Test
    void testCreate_Fail_InvalidUsername() {
        virtualAccessDTO.setUsername("");

        assertThrows(InvalidInputException.class, () -> virtualAccessService.create(virtualAccessDTO));
    }

    @Test
    void testCreate_Fail_InvalidPassword() {
        virtualAccessDTO.setPassword("");

        assertThrows(InvalidInputException.class, () -> virtualAccessService.create(virtualAccessDTO));
    }

    @Test
    void testUpdate_Success() {
        when(virtualAccessRepository.findById(1L)).thenReturn(Optional.of(virtualAccess));
        when(virtualAccessRepository.save(any(VirtualAccess.class))).thenReturn(virtualAccess);

        virtualAccessDTO.setUsername("newUser");
        virtualAccessDTO.setPassword("newPass");
        VirtualAccessDTO result = virtualAccessService.update(1L, virtualAccessDTO);

        assertNotNull(result);
        assertEquals(virtualAccessDTO.getUsername(), result.getUsername());
        assertEquals(virtualAccessDTO.getPassword(), result.getPassword());
    }

    @Test
    void testUpdate_Fail_NullVirtualAccessDTO() {
        when(virtualAccessRepository.findById(1L)).thenReturn(Optional.of(virtualAccess));

        assertThrows(NullPointerException.class, () -> virtualAccessService.update(1L, null));
    }

    @Test
    void testUpdate_Fail_InvalidUsername() {
        when(virtualAccessRepository.findById(1L)).thenReturn(Optional.of(virtualAccess));
        virtualAccessDTO.setUsername("");

        assertThrows(InvalidInputException.class, () -> virtualAccessService.update(1L, virtualAccessDTO));
    }

    @Test
    void testUpdate_Fail_InvalidPassword() {
        when(virtualAccessRepository.findById(1L)).thenReturn(Optional.of(virtualAccess));
        virtualAccessDTO.setPassword("");

        assertThrows(InvalidInputException.class, () -> virtualAccessService.update(1L, virtualAccessDTO));
    }


    @Test
    void testDelete_Success() {
        when(virtualAccessRepository.findById(1L)).thenReturn(Optional.of(virtualAccess));

        virtualAccessService.deleteById(1L);

        verify(virtualAccessRepository, times(1)).delete(virtualAccess);
    }

    @Test
    void testDelete_Fail_VirtualAccessNotFound() {
        when(virtualAccessRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(VirtualAccessNotFoundException.class, () -> virtualAccessService.deleteById(1L));
    }
}

