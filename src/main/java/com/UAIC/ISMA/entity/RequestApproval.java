package com.UAIC.ISMA.entity;

import com.UAIC.ISMA.entity.enums.ApprovalStatus;
import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "request_approvals")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestApproval implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus approvalStatus;

    private LocalDateTime approvalDate = LocalDateTime.now();

    private String comments;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private AccessRequest accessRequest;

    @ManyToOne
    @JoinColumn(name = "approver_id")
    private User approver;

    @PrePersist
    public void onPrePersist() {
        this.approvalStatus = ApprovalStatus.PENDING;
        this.approvalDate = LocalDateTime.now();
    }

    public RequestApproval(ApprovalStatus approvalStatus, AccessRequest accessRequest, User approver, String comments) {
        this();
        this.approvalStatus = approvalStatus;
        this.accessRequest = accessRequest;
        this.approver = approver;
        this.comments = comments;
    }
}
