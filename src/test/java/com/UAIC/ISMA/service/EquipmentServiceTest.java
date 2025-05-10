package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dao.Equipment;
import com.UAIC.ISMA.dao.Laboratory;
import com.UAIC.ISMA.dao.enums.AvailabilityStatus;
import com.UAIC.ISMA.dto.EquipmentDTO;
import com.UAIC.ISMA.exception.EquipmentNotFoundException;
import com.UAIC.ISMA.repository.EquipmentRepository;
import com.UAIC.ISMA.repository.LaboratoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class EquipmentServiceTest {
    @Mock
    private EquipmentRepository equipmentRepository;

    @Mock
    private LaboratoryRepository laboratoryRepository;

    @InjectMocks
    private EquipmentService equipmentService;

    private Equipment equipment;
    private EquipmentDTO equipmentDTO;
    private Laboratory laboratory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        laboratory = new Laboratory();
        laboratory.setId(10L);

        equipment = new Equipment();
        equipment.setId(1L);
        equipment.setName("Oscilloscope");
        equipment.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        equipment.setLaboratory(laboratory);

        equipmentDTO = new EquipmentDTO();
        equipmentDTO.setId(1L);
        equipmentDTO.setName("Oscilloscope");
        equipmentDTO.setAvailabilityStatus(AvailabilityStatus.AVAILABLE);
        equipmentDTO.setLaboratoryId(10L);

    }

    @Test
    void testGetEquipmentByID_Success() {
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));

        EquipmentDTO result = equipmentService.getEquipmentById(1L);

        assertNotNull(result);
        assertEquals(equipment.getName(), result.getName());
        assertEquals(equipment.getLaboratory().getId(), result.getLaboratoryId());

    }

    @Test
    void testGetEquipmentById_NotFound() {
        when(equipmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EquipmentNotFoundException.class, () -> equipmentService.getEquipmentById(1L));
    }

    @Test
    void testCreateEquipment_Success() {
        when(laboratoryRepository.findById(10L)).thenReturn(Optional.of(laboratory));
        when(equipmentRepository.save(any(Equipment.class))).thenReturn(equipment);

        EquipmentDTO result = equipmentService.createEquipment(equipmentDTO);

        assertNotNull(result);
        assertEquals("Oscilloscope", result.getName());
    }

    @Test
    void testCreateEquipment_Fail_NullDTO(){
        assertThrows(NullPointerException.class, () -> equipmentService.createEquipment(null));
    }

    @Test
    void testUpdateEquipment_Success() {
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));
        when(laboratoryRepository.findById(10L)).thenReturn(Optional.of(laboratory));
        when(equipmentRepository.save(any(Equipment.class))).thenReturn(equipment);

        EquipmentDTO result = equipmentService.updateEquipment( equipmentDTO, 1L);

        assertNotNull(result);
        assertEquals("Oscilloscope", result.getName());
    }

    @Test
    void testUpdateEquipment_NotFound() {
        when(equipmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EquipmentNotFoundException.class, () -> equipmentService.updateEquipment(equipmentDTO, 1L));
    }

    @Test
    void testUpdateEquipment_Fail_NullDTO() {
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));

        assertThrows(NullPointerException.class, () -> equipmentService.updateEquipment(null, 1L));
    }

    @Test
    void testDeleteEquipment_Success() {
        when(equipmentRepository.findById(1L)).thenReturn(Optional.of(equipment));

        equipmentService.deleteEquipment(1L);

        verify(equipmentRepository, times(1)).delete(equipment);
    }

    @Test
    void testDeleteEquipment_NotFound() {
        when(equipmentRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(EquipmentNotFoundException.class, () -> equipmentService.deleteEquipment(1L));
    }

    @Test
    void testGetAllEquipment_Success() {
        when(equipmentRepository.findAll()).thenReturn(List.of(equipment));

        List<EquipmentDTO> result = equipmentService.getAllEquipments(null);

        assertEquals(1, result.size());
        assertEquals("Oscilloscope", result.get(0).getName());
    }

    @Test
    void testGetAllEquipment_Empty() {
        when(equipmentRepository.findAll()).thenReturn(List.of());

        List<EquipmentDTO> result = equipmentService.getAllEquipments(null);

        assertTrue(result.isEmpty());
    }
}
