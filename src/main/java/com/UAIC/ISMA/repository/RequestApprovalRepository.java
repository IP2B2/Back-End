package com.UAIC.ISMA.repository;

import com.UAIC.ISMA.entity.RequestApproval;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RequestApprovalRepository extends JpaRepository<RequestApproval, Long> {
    List<RequestApproval> findByAccessRequest_IdAndApprover_Id(Long accessRequestId, Long approverId);
    List<RequestApproval> findByApprover_Id(Long approverId);
    List<RequestApproval> findByAccessRequest_Id(Long accessRequestId);
}