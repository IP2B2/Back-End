package com.UAIC.ISMA.dao;


import jakarta.persistence.*;

import java.io.Serializable;

@Entity
@Table(name = "equipment")
public class Equipment implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "photo_url")
    private String photoUrl;

    @Column(name = "inventory_number", nullable = false)
    private String inventoryNumber;

    @Column(name = "acquisition_date", nullable = false)
    private String acquisitionDate;

    @Column(name = "availability_status", nullable = false)
    private String availabilityStatus;

    @Column(name = "access_requirements", nullable = false)
    private String accessRequirements;

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

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public String getInventoryNumber() {
        return inventoryNumber;
    }

    public void setInventoryNumber(String inventoryNumber) {
        this.inventoryNumber = inventoryNumber;
    }

    public String getAcquisitionDate() {
        return acquisitionDate;
    }

    public void setAcquisitionDate(String acquisitionDate) {
        this.acquisitionDate = acquisitionDate;
    }

    public String getAvailabilityStatus() {
        return availabilityStatus;
    }

    public void setAvailabilityStatus(String availabilityStatus) {
        this.availabilityStatus = availabilityStatus;
    }

    public String getAccessRequirements() {
        return accessRequirements;
    }

    public void setAccessRequirements(String accessRequirements) {
        this.accessRequirements = accessRequirements;
    }
}
