package com.UAIC.ISMA.repository;

import com.UAIC.ISMA.dao.AccessRequest;
import com.UAIC.ISMA.dao.enums.RequestStatus;
import com.UAIC.ISMA.dto.AccessRequestDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

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
}