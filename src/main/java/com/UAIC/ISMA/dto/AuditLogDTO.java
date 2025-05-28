package com.UAIC.ISMA.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLogDTO {
    private Long id;

    @NotBlank(message = "Action is required.")
    private String action;

    @NotBlank(message = "Details are required.")
    private String details;

    private LocalDateTime timestamp;

    @NotNull(message = "User ID is required.")
    private Long userId;
}
