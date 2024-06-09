package com.example.localguidebe.service;

import com.example.localguidebe.entity.Role;
import com.example.localguidebe.enums.RolesEnum;

import java.util.List;

public interface RoleService {
    public List<Role> getRoleList();

    Role findByName(RolesEnum rolesEnum);

    Role save(Role role);
}
