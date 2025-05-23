package com.UAIC.ISMA.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class VirtualAccessDTO {
    private Long id;
    private String username;
    private String password;
    private LocalDateTime issuedDate;
    private Long accessRequestId;

}
