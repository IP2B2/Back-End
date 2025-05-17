package com.UAIC.ISMA.repository;

import com.UAIC.ISMA.dao.Role;
import com.UAIC.ISMA.dao.User;
import com.UAIC.ISMA.dao.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
    Optional<Role> findByRoleName(RoleName roleName);
}