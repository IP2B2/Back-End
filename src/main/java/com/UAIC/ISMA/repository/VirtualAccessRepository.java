package com.UAIC.ISMA.repository;

import com.UAIC.ISMA.entity.VirtualAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface VirtualAccessRepository extends JpaRepository<VirtualAccess, Long> {
    boolean existsByAccessRequest_Id(Long accessRequestId);
    Optional<VirtualAccess> findByAccessRequest_Id(Long accessRequestId);

}
