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
import java.time.LocalDateTime;


@Repository
public interface AccessRequestRepository extends JpaRepository<AccessRequest, Long> {

    @Query("""
    SELECT new com.UAIC.ISMA.dto.AccessRequestDTO(
        ar.id, ar.requestDate, ar.status, ar.requestType,
        ar.proposalFile, ar.expectedReturnDate,
        ar.user.id, ar.equipment.id)
    FROM AccessRequest ar
    WHERE (:status IS NULL OR ar.status = :status)
      AND (:equipmentType IS NULL OR ar.equipment.name LIKE :equipmentType)
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
            "AND (:dateStart IS NULL OR ar.requestDate >= :dateStart) " +
            "AND (:dateEnd IS NULL OR ar.requestDate < :dateEnd)")
    Page<AccessRequestDTO> findDTOByUserWithFilters(
            @Param("userId") Long userId,
            @Param("status") RequestStatus status,
            @Param("dateStart") LocalDateTime dateStart,
            @Param("dateEnd") LocalDateTime dateEnd,
            Pageable pageable
    );


    @Query("""
                SELECT new com.UAIC.ISMA.dto.AccessRequestDTO(
                    ar.id, ar.requestDate, ar.status, ar.requestType,
                    ar.proposalFile, ar.expectedReturnDate,
                    ar.user.id, ar.equipment.id)
                FROM AccessRequest ar
                WHERE ar.user.id = :userId
            """)
    Page<AccessRequestDTO> findByUserId(
            @Param("userId") Long userId,
            Pageable pageable
    );
    @Query("""
SELECT new com.UAIC.ISMA.dto.AccessRequestDTO(
    ar.id, ar.requestDate, ar.status, ar.requestType,
    ar.proposalFile, ar.expectedReturnDate,
    ar.user.id, ar.equipment.id)
FROM AccessRequest ar
WHERE ar.user.id = :userId
AND ar.status = :status
AND ar.requestDate >= :start
AND ar.requestDate < :end
""")
    Page<AccessRequestDTO> findByUserIdAndStatusAndDateBetween(
            @Param("userId") Long userId,
            @Param("status") RequestStatus status,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable
    );

    @Query("""
SELECT new com.UAIC.ISMA.dto.AccessRequestDTO(
    ar.id, ar.requestDate, ar.status, ar.requestType,
    ar.proposalFile, ar.expectedReturnDate,
    ar.user.id, ar.equipment.id)
FROM AccessRequest ar
WHERE ar.user.id = :userId
AND ar.status = :status
""")
    Page<AccessRequestDTO> findByUserIdAndStatus(
            @Param("userId") Long userId,
            @Param("status") RequestStatus status,
            Pageable pageable
    );

    @Query("""
SELECT new com.UAIC.ISMA.dto.AccessRequestDTO(
    ar.id, ar.requestDate, ar.status, ar.requestType,
    ar.proposalFile, ar.expectedReturnDate,
    ar.user.id, ar.equipment.id)
FROM AccessRequest ar
WHERE ar.user.id = :userId
AND ar.requestDate >= :start
AND ar.requestDate < :end
""")
    Page<AccessRequestDTO> findByUserIdAndDateBetween(
            @Param("userId") Long userId,
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            Pageable pageable
    );






}