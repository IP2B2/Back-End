package com.UAIC.ISMA.dto;

import java.time.LocalDateTime;

public class RequestApprovalDTO {
    private Long id;
    private String approvalStatus;
    private LocalDateTime approvalDate;
    private String comments;
    private Long accessRequestId;
    private Long approverId;

    public RequestApprovalDTO() {
    }

    public RequestApprovalDTO(Long id, String approvalStatus, LocalDateTime approvalDate, String comments, Long accessRequestId, Long approverId) {
        this.id = id;
        this.approvalStatus = approvalStatus;
        this.approvalDate = approvalDate;
        this.comments = comments;
        this.accessRequestId = accessRequestId;
        this.approverId = approverId;
    }

    public RequestApprovalDTO(Long id, String approvalStatus, LocalDateTime approvalDate, String comments) {
        this.id = id;
        this.approvalStatus = approvalStatus;
        this.approvalDate = approvalDate;
        this.comments = comments;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getApprovalStatus() {
        return approvalStatus;
    }

    public void setApprovalStatus(String approvalStatus) {
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

    public Long getAccessRequestId() {
        return accessRequestId;
    }

    public void setAccessRequestId(Long accessRequestId) {
        this.accessRequestId = accessRequestId;
    }

    public Long getApproverId() {
        return approverId;
    }

    public void setApproverId(Long approverId) {
        this.approverId = approverId;
    }
}
