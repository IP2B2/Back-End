package com.UAIC.ISMA.entity;


import com.UAIC.ISMA.entity.enums.ApprovalStatus;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "request_approvals")
public class RequestApproval implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ApprovalStatus approvalStatus;

    private LocalDateTime approvalDate;

    private String comments;

    @ManyToOne
    @JoinColumn(name = "request_id")
    private AccessRequest accessRequest;

    @ManyToOne
    @JoinColumn(name = "approver_id")
    private User approver;

    public RequestApproval() {
        this.approvalDate = LocalDateTime.now();
    }

    public RequestApproval(ApprovalStatus approvalStatus, AccessRequest accessRequest, User approver, String comments) {
        this();
        this.approvalStatus = approvalStatus;
        this.accessRequest = accessRequest;
        this.approver = approver;
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ApprovalStatus getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(ApprovalStatus approvalStatus) {
        this.approvalStatus = approvalStatus;
    }

    public LocalDateTime getApprovalDate() {
        return approvalDate;
    }

    public void setApprovalDate(LocalDateTime approvalDate) {
        this.approvalDate = approvalDate;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public AccessRequest getAccessRequest() {
        return accessRequest;
    }

    public void setAccessRequest(AccessRequest accessRequest) {
        this.accessRequest = accessRequest;
    }

    public User getApprover() {
        return approver;
    }

    public void setApprover(User approver) {
        this.approver = approver;
    }
}