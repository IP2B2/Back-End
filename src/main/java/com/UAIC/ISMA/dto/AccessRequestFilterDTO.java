package com.UAIC.ISMA.dto;

import com.UAIC.ISMA.entity.enums.RequestStatus;
import jakarta.validation.constraints.Min;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class AccessRequestFilterDTO {
    private RequestStatus status;
    private String equipmentType;
    private Long userId;

    @Min(0)
    private int page = 0;

    @Min(1)
    private int size = 10;
}
