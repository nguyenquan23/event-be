package com.example.localguidebe.repository;

import com.example.localguidebe.entity.Role;
import com.example.localguidebe.enums.RolesEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Integer> {
    Role findByName(RolesEnum name);
}
