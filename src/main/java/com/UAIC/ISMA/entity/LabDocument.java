package com.UAIC.ISMA.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LabDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String filename;

    private String fileType;

    private String version;

    private String filePath;

    private boolean archived;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lab_id")
    private Laboratory lab;
}
