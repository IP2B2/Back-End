package com.UAIC.ISMA.dto;

import com.UAIC.ISMA.entity.enums.RequestStatus;
import com.UAIC.ISMA.entity.enums.RequestType;

import java.time.LocalDateTime;

public class AccessRequestDTO {
    private Long id;
    private LocalDateTime requestDate;
    private RequestStatus status;
    private RequestType requestType;
    private String proposalFile;
    private LocalDateTime expectedReturnDate;
    private Long userId;
    private Long equipmentId;

    public AccessRequestDTO() {
    }

    public AccessRequestDTO(Long id, LocalDateTime requestDate, RequestStatus status,
                            RequestType requestType, String proposalFile,
                            LocalDateTime expectedReturnDate, Long userId, Long equipmentId) {
        this.id = id;
        this.requestDate = requestDate;
        this.status = status;
        this.requestType = requestType;
        this.proposalFile = proposalFile;
        this.expectedReturnDate = expectedReturnDate;
        this.userId = userId;
        this.equipmentId = equipmentId;
    }

    // Gettere și settere
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getRequestDate() {
        return requestDate;
    }

    public void setRequestDate(LocalDateTime requestDate) {
        this.requestDate = requestDate;
    }

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public String getProposalFile() {
        return proposalFile;
    }

    public void setProposalFile(String proposalFile) {
        this.proposalFile = proposalFile;
    }

    public LocalDateTime getExpectedReturnDate() {
        return expectedReturnDate;
    }

    public void setExpectedReturnDate(LocalDateTime expectedReturnDate) {
        this.expectedReturnDate = expectedReturnDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(Long equipmentId) {
        this.equipmentId = equipmentId;
    }
}
