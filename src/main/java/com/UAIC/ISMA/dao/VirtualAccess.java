package com.UAIC.ISMA.dao;


import jakarta.persistence.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "virtual_access")
public class VirtualAccess implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    private String password; // stocat criptat

    private LocalDateTime issuedDate;

    @OneToOne
    @JoinColumn(name = "request_id")
    private AccessRequest accessRequest;

    public VirtualAccess() {
        this.issuedDate = LocalDateTime.now();
    }

    public VirtualAccess(String username, String password, AccessRequest accessRequest) {
        this();
        this.username = username;
        this.password = password;
        this.accessRequest = accessRequest;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getIssuedDate() {
        return issuedDate;
    }

    public void setIssuedDate(LocalDateTime issuedDate) {
        this.issuedDate = issuedDate;
    }

    public AccessRequest getAccessRequest() {
        return accessRequest;
    }

    public void setAccessRequest(AccessRequest accessRequest) {
        this.accessRequest = accessRequest;
    }

    // equals, hashCode and toString

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VirtualAccess)) return false;
        VirtualAccess that = (VirtualAccess) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "VirtualAccess{" +
                "id=" + id +
                ", username='" + username + '\'' +
                '}';
    }
}