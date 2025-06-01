package com.UAIC.ISMA.dto;

import com.UAIC.ISMA.entity.enums.RequestStatus;
import com.UAIC.ISMA.entity.enums.RequestType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AccessRequestDTO {
    private Long id;
    private LocalDateTime requestDate;
    private RequestStatus status;
    private RequestType requestType;
    private String proposalFile;
    private LocalDateTime expectedReturnDate;
    private Long userId;
    private Long equipmentId;
    private String borrowerCNP;
    private String borrowerAddress;
}
