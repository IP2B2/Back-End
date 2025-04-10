package com.UAIC.ISMA.repository;

import com.UAIC.ISMA.dao.VirtualAccess;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VirtualAccessRepository extends JpaRepository<VirtualAccess, Long> {
}
