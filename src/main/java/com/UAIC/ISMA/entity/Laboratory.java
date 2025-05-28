package com.UAIC.ISMA.entity;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.List;

@Entity
@Table(name = "laboratories")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Laboratory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String labName;

    private String description;
    private String location;

    @OneToMany(mappedBy = "laboratory", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private List<Equipment> equipments;

    @OneToMany(mappedBy = "lab")
    private List<LabDocument> labDocuments;

    public Laboratory(String labName, String description, String location) {
        this.labName = labName;
        this.description = description;
        this.location = location;
    }
}
