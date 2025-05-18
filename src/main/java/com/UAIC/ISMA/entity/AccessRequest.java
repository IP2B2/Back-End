package com.UAIC.ISMA.entity;

import com.UAIC.ISMA.entity.enums.RequestStatus;
import com.UAIC.ISMA.entity.enums.RequestType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "access_requests")
public class AccessRequest implements Serializable {
  
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(nullable = false)
    private LocalDateTime requestDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestType requestType;

    @Size(max = 255)
    @Column(length = 255)
    private String proposalFile;

    private LocalDateTime expectedReturnDate;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @NotNull
    @ManyToOne(optional = false)
    @JoinColumn(name = "equipment_id", nullable = false)
    private Equipment equipment;

    @OneToMany(mappedBy = "accessRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequestDocument> requestDocuments;

    @OneToMany(mappedBy = "accessRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RequestApproval> requestApprovals;

    @OneToOne(mappedBy = "accessRequest", cascade = CascadeType.ALL, orphanRemoval = true)
    private VirtualAccess virtualAccess;

  
    public AccessRequest() {
        this.requestDate = LocalDateTime.now();
    }

    public AccessRequest(User user, Equipment equipment, RequestStatus status, RequestType requestType) {
        this();
        this.user = user;
        this.equipment = equipment;
        this.status = status;
        this.requestType = requestType;
    }


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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Equipment getEquipment() {
        return equipment;
    }

    public void setEquipment(Equipment equipment) {
        this.equipment = equipment;
    }

    public List<RequestDocument> getRequestDocuments() {
        return requestDocuments;
    }

    public void setRequestDocuments(List<RequestDocument> requestDocuments) {
        this.requestDocuments = requestDocuments;
    }

    public List<RequestApproval> getRequestApprovals() {
        return requestApprovals;
    }

    public void setRequestApprovals(List<RequestApproval> requestApprovals) {
        this.requestApprovals = requestApprovals;
    }

    public VirtualAccess getVirtualAccess() {
        return virtualAccess;
    }

    public void setVirtualAccess(VirtualAccess virtualAccess) {
        this.virtualAccess = virtualAccess;
    }
}