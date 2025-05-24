package com.UAIC.ISMA.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RequestApprovalDTO {
    private Long id;
    private String approvalStatus;
    private LocalDateTime approvalDate;
    private String comments;
    private Long accessRequestId;
    private Long approverId;
}
