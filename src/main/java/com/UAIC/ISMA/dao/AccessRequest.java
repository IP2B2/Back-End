package com.UAIC.ISMA.dao;


import com.UAIC.ISMA.dao.enums.RequestStatus;
import com.UAIC.ISMA.dao.enums.RequestType;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "access_requests")
public class AccessRequest implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime requestDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RequestType requestType;

    private String proposalFile;

    private LocalDateTime expectedReturnDate;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne
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


    public Long getId() {
        return id;
    }
}