package com.hive5.ds.repositories.roles;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hive5.ds.entities.role.Role;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    
}
