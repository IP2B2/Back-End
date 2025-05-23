package com.UAIC.ISMA.service;


import com.UAIC.ISMA.entity.Laboratory;
import com.UAIC.ISMA.dto.LaboratoryDTO;
import com.UAIC.ISMA.exception.InvalidInputException;
import com.UAIC.ISMA.exception.LaboratoryNotFoundException;
import com.UAIC.ISMA.repository.LaboratoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LaboratoryServiceTest {

    @Mock
    private LaboratoryRepository laboratoryRepository;

    @InjectMocks
    private LaboratoryService laboratoryService;

    private Laboratory laboratory;
    private LaboratoryDTO laboratoryDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        laboratory = new Laboratory();
        laboratory.setId(1L);
        laboratory.setLabName("Lab A");
        laboratory.setLocation("Building X");
        laboratory.setDescription("Electronics lab");

        laboratoryDTO = new LaboratoryDTO();
        laboratoryDTO.setId(1L);
        laboratoryDTO.setLabName("Lab A");
        laboratoryDTO.setLocation("Building X");
        laboratoryDTO.setDescription("Electronics lab");
    }

    @Test
    void testGetLaboratoryById_Success() {
        when(laboratoryRepository.findById(1L)).thenReturn(Optional.of(laboratory));

        LaboratoryDTO result = laboratoryService.getLaboratoryById(1L);

        assertNotNull(result);
        assertEquals(laboratory.getId(), result.getId());
        assertEquals(laboratory.getLabName(), result.getLabName());
        assertEquals(laboratory.getLocation(), result.getLocation());
    }

    @Test
    void testGetLaboratoryById_NotFound() {
        when(laboratoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(LaboratoryNotFoundException.class, () -> laboratoryService.getLaboratoryById(1L));
    }

    @Test
    void testCreateLaboratory_Success() {
        when(laboratoryRepository.save(any(Laboratory.class))).thenReturn(laboratory);

        LaboratoryDTO result = laboratoryService.createLaboratory(laboratoryDTO);

        assertNotNull(result);
        assertEquals(laboratory.getLabName(), result.getLabName());
        assertEquals(laboratory.getLocation(), result.getLocation());
        assertEquals(laboratory.getDescription(), result.getDescription());
    }

    @Test
    void testCreateLaboratory_NullDTO() {
        assertThrows(NullPointerException.class, () -> laboratoryService.createLaboratory(null));
    }

    @Test
    void testUpdateLaboratory_Success() {
        when(laboratoryRepository.findById(1L)).thenReturn(Optional.of(laboratory));
        when(laboratoryRepository.save(any(Laboratory.class))).thenReturn(laboratory);

        laboratoryDTO.setLabName("Updated Lab");
        LaboratoryDTO result = laboratoryService.updateLaboratory(1L, laboratoryDTO);

        assertNotNull(result);
        assertEquals("Updated Lab", result.getLabName());
    }

    @Test
    void testUpdateLaboratory_NotFound() {
        when(laboratoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(LaboratoryNotFoundException.class, () -> laboratoryService.updateLaboratory(1L, laboratoryDTO));
    }

    @Test
    void testUpdateLaboratory_NullDTO() {
        when(laboratoryRepository.findById(1L)).thenReturn(Optional.of(laboratory));

        assertThrows(NullPointerException.class, () -> laboratoryService.updateLaboratory(1L, null));
    }

    @Test
    void testDeleteLaboratory_Success() {
        when(laboratoryRepository.findById(1L)).thenReturn(Optional.of(laboratory));

        laboratoryService.deleteLaboratory(1L);

        verify(laboratoryRepository, times(1)).delete(laboratory);
    }

    @Test
    void testDeleteLaboratory_NotFound() {
        when(laboratoryRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(LaboratoryNotFoundException.class, () -> laboratoryService.deleteLaboratory(1L));
    }

    @Test
    void testGetAllLaboratories_Success() {
        Laboratory lab2 = new Laboratory();
        lab2.setId(2L);
        lab2.setLabName("Lab B");
        lab2.setLocation("Building Y");

        when(laboratoryRepository.findAll()).thenReturn(List.of(laboratory, lab2));

        List<LaboratoryDTO> result = laboratoryService.getAlLaboratories();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
    }

    @Test
    void testGetAllLaboratories_Empty() {
        when(laboratoryRepository.findAll()).thenReturn(List.of());

        List<LaboratoryDTO> result = laboratoryService.getAlLaboratories();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void testSearchLaboratoriesAllFilters_Success() {
        String name = laboratoryDTO.getLabName(); // "Lab A"
        String location = laboratoryDTO.getLocation(); // "Building X"
        Pageable pageable = PageRequest.of(0, 10);

        Page<LaboratoryDTO> page = new PageImpl<>(List.of(laboratoryDTO));

        when(laboratoryRepository.searchLaboratoryByNameAndLocation(eq(name), eq(location), eq(pageable)))
                .thenReturn(page);

        Page<LaboratoryDTO> result = laboratoryService.searchLaboratories(name, location, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Lab A", result.getContent().get(0).getLabName());
        assertEquals("Building X", result.getContent().get(0).getLocation());
    }

    @Test
    void testSearchLaboratoriesByNameOnly_Success() {
        String name = laboratoryDTO.getLabName(); // "Lab A"
        String location = null;
        Pageable pageable = PageRequest.of(0, 10);

        Page<LaboratoryDTO> page = new PageImpl<>(List.of(laboratoryDTO));

        when(laboratoryRepository.searchLaboratoryByNameAndLocation(eq(name), isNull(), eq(pageable)))
                .thenReturn(page);

        Page<LaboratoryDTO> result = laboratoryService.searchLaboratories(name, location, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Lab A", result.getContent().get(0).getLabName());
        assertEquals("Building X", result.getContent().get(0).getLocation());
    }

    @Test
    void testSearchLaboratoriesByLocationOnly_Success() {
        String name = null;
        String location = laboratoryDTO.getLocation(); // "Building X"
        Pageable pageable = PageRequest.of(0, 10);

        Page<LaboratoryDTO> page = new PageImpl<>(List.of(laboratoryDTO));

        when(laboratoryRepository.searchLaboratoryByNameAndLocation(isNull(), eq(location), eq(pageable)))
                .thenReturn(page);

        Page<LaboratoryDTO> result = laboratoryService.searchLaboratories(name, location, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Lab A", result.getContent().get(0).getLabName());
        assertEquals("Building X", result.getContent().get(0).getLocation());
    }

    @Test
    void testSearchLaboratoriesWithNoFilters_Success() {
        Pageable pageable = PageRequest.of(0, 10);

        when(laboratoryRepository.searchLaboratoryByNameAndLocation(isNull(), isNull(), eq(pageable)))
                .thenReturn(new PageImpl<>(List.of(laboratoryDTO)));

        Page<LaboratoryDTO> result = laboratoryService.searchLaboratories(null, null, pageable);

        assertEquals(1, result.getTotalElements());
        assertEquals("Lab A", result.getContent().get(0).getLabName());
        assertEquals("Building X", result.getContent().get(0).getLocation());
    }

    @Test
    void testSearchLaboratories_InvalidLocation() {
        String invalidLocation = "@@!!!Etaj##";
        Pageable pageable = PageRequest.of(0, 10);

        assertThrows(InvalidInputException.class, () ->
                laboratoryService.searchLaboratories(null, invalidLocation, pageable));
    }

    @Test
    void testSearchLaboratories_InvalidName() {
        String invalidName = "";
        Pageable pageable = PageRequest.of(0, 10);

        assertThrows(InvalidInputException.class, () ->
                laboratoryService.searchLaboratories(invalidName, null, pageable));
    }

}
