package com.UAIC.ISMA.dto;

import com.UAIC.ISMA.dao.enums.RoleName;

public class RoleDTO {
    private Long id;
    private RoleName roleName;

    public RoleDTO() {
    }

    public RoleDTO(Long id, RoleName roleName) {
        this.id = id;
        this.roleName = roleName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public RoleName getRoleName() {
        return roleName;
    }

    public void setRoleName(RoleName roleName) {
        this.roleName = roleName;
    }
}
