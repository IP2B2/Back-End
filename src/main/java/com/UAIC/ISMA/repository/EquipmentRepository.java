package com.UAIC.ISMA.repository;

import com.UAIC.ISMA.dto.EquipmentDTO;
import com.UAIC.ISMA.entity.Equipment;
import com.UAIC.ISMA.entity.enums.AvailabilityStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Long> {

    @Query("""
        SELECT new com.UAIC.ISMA.dto.EquipmentDTO(
            e.id, e.name, e.photo, e.inventoryNumber,
            e.acquisitionDate, e.availabilityStatus,
            e.laboratory.id, e.accessRequirements)
        FROM Equipment e
        WHERE (:name IS NULL OR LOWER(e.name) LIKE LOWER(CONCAT('%', :name, '%')))
          AND (:status IS NULL OR e.availabilityStatus = :status)
          AND (:labId IS NULL OR e.laboratory.id = :labId)
    """)
    Page<EquipmentDTO> searchByNameStatusAndLabId(
            @Param("name") String name,
            @Param("status") AvailabilityStatus status,
            @Param("labId") Long labId,
            Pageable pageable
    );
}
