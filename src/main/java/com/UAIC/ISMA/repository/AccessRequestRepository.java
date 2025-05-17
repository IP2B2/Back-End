package com.UAIC.ISMA.repository;

import com.UAIC.ISMA.entity.AccessRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccessRequestRepository extends JpaRepository<AccessRequest, Long> {
}
