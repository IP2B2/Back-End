package com.UAIC.ISMA.dto;

import com.UAIC.ISMA.dao.enums.RequestStatus;
import jakarta.validation.constraints.Min;

public class AccessRequestFilterDTO {
    private RequestStatus status;
    private String equipmentType;
    private Long userId;

    @Min(0)
    private int page = 0;

    @Min(1)
    private int size = 10;

    public RequestStatus getStatus() {
        return status;
    }

    public void setStatus(RequestStatus status) {
        this.status = status;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public int getPage() {
        return page;
    }

    public void setPage(int page) {
        this.page = page;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}