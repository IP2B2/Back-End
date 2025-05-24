package com.UAIC.ISMA.entity;


import com.UAIC.ISMA.entity.enums.AvailabilityStatus;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "equipment")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @Column(length = 5000)
    private String accessRequirements;

    @ManyToOne
    @JoinColumn(name = "lab_id")
    private Laboratory laboratory;

    @OneToMany(mappedBy = "equipment", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<AccessRequest> accessRequests;

    public Equipment(String name, String inventoryNumber, LocalDate acquisitionDate,
                     AvailabilityStatus availabilityStatus, String accessRequirements, Laboratory laboratory) {
        this.name = name;
        this.inventoryNumber = inventoryNumber;
        this.acquisitionDate = acquisitionDate;
        this.availabilityStatus = availabilityStatus;
        this.accessRequirements = accessRequirements;
        this.laboratory = laboratory;
    }
}
