package com.UAIC.ISMA.repository;

import com.UAIC.ISMA.dao.AccessRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.UAIC.ISMA.dao.enums.RequestStatus;

@Repository
public interface AccessRequestRepository extends JpaRepository<AccessRequest, Long> {

    @Query("SELECT ar FROM AccessRequest ar WHERE ar.user.id = :userId " +
            "AND (:status IS NULL OR ar.status = :status) " +
            "AND (:date IS NULL OR FUNCTION('DATE', ar.requestDate) = :date)")
    Page<AccessRequest> findByUserWithFilters(
            @Param("userId") Long userId,
            @Param("status") RequestStatus status,
            @Param("date") java.time.LocalDate date,
            Pageable pageable
    );
}
