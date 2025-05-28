package com.UAIC.ISMA.entity;

import com.UAIC.ISMA.entity.enums.RequestStatus;
import com.UAIC.ISMA.entity.enums.RequestType;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "access_requests")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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
}
