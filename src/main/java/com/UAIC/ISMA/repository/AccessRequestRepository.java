package com.UAIC.ISMA.repository;

import com.UAIC.ISMA.entity.AccessRequest;
import com.UAIC.ISMA.dto.AccessRequestDTO;
import com.UAIC.ISMA.entity.User;
import com.UAIC.ISMA.entity.enums.RequestStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AccessRequestRepository extends JpaRepository<AccessRequest, Long> {

    @Query("""
        SELECT new com.UAIC.ISMA.dto.AccessRequestDTO(
            ar.id, ar.requestDate, ar.status, ar.requestType,
            ar.proposalFile, ar.expectedReturnDate,
            ar.user.id, ar.equipment.id)
        FROM AccessRequest ar
        WHERE (:status IS NULL OR ar.status = :status)
        AND (:equipmentType IS NULL OR LOWER(ar.equipment.name) LIKE LOWER(CONCAT('%', :equipmentType, '%')))
        AND (:userId IS NULL OR ar.user.id = :userId)
    """)
    Page<AccessRequestDTO> filterAccessRequests(
            @Param("status") RequestStatus status,
            @Param("equipmentType") String equipmentType,
            @Param("userId") Long userId,
            Pageable pageable
    );


    @Query("SELECT new com.UAIC.ISMA.dto.AccessRequestDTO(" +
            "ar.id, ar.requestDate, ar.status, ar.requestType, " +
            "ar.proposalFile, ar.expectedReturnDate, " +
            "ar.user.id, ar.equipment.id) " +
            "FROM AccessRequest ar " +
            "WHERE ar.user.id = :userId " +
            "AND (:status IS NULL OR ar.status = :status) " +
            "AND (:date IS NULL OR CAST(ar.requestDate AS date) = :date)")
    Page<AccessRequestDTO> findDTOByUserWithFilters(
            @Param("userId") Long userId,
            @Param("status") RequestStatus status,
            @Param("date") LocalDate date,
            Pageable pageable
    );
}