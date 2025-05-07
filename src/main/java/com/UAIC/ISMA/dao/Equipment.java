package com.UAIC.ISMA.dao;


import com.UAIC.ISMA.dao.enums.AvailabilityStatus;
import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "equipment")
public class Equipment implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    private String photo;

    private String inventoryNumber;

    private LocalDate acquisitionDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AvailabilityStatus availabilityStatus;

    private String accessRequirements;

    @ManyToOne
    @JoinColumn(name = "lab_id")
    private Laboratory laboratory;

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccessRequest> accessRequests;

    public Equipment() {
    }

    public Equipment(String name, String inventoryNumber, LocalDate acquisitionDate,
                     AvailabilityStatus availabilityStatus, String accessRequirements, Laboratory laboratory) {
        this.name = name;
        this.inventoryNumber = inventoryNumber;
        this.acquisitionDate = acquisitionDate;
        this.availabilityStatus = availabilityStatus;
        this.accessRequirements = accessRequirements;
        this.laboratory = laboratory;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public LocalDate getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(LocalDate acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public AvailabilityStatus getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(AvailabilityStatus availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public Laboratory getLaboratory() { return laboratory; }

    public void setLaboratory(Laboratory laboratory) { this.laboratory = laboratory; }

    public String getAccessRequirements() { return accessRequirements; }

    public void setAccessRequirements(String accessRequirements) { this.accessRequirements = accessRequirements; }

    public List<AccessRequest> getAccessRequests() { return accessRequests; }

    public void setAccessRequests(List<AccessRequest> accessRequests) { this.accessRequests = accessRequests; }
}
