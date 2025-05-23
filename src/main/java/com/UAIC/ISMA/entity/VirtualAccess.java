package com.UAIC.ISMA.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "virtual_access")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class VirtualAccess implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password;

    private LocalDateTime issuedDate;

    @OneToOne
    @JoinColumn(name = "request_id")
    private AccessRequest accessRequest;

    public VirtualAccess(String username, String password, AccessRequest accessRequest) {
        this.username = username;
        this.password = password;
        this.accessRequest = accessRequest;
        this.issuedDate = LocalDateTime.now();
    }
}