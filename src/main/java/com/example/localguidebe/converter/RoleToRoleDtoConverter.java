package com.example.localguidebe.converter;

import com.example.localguidebe.entity.Role;
import com.example.localguidebe.enums.RolesEnum;
import org.springframework.stereotype.Component;

@Component
public class RoleToRoleDtoConverter {
    public RolesEnum convert(Role source) {
        return source.getName();
    }
}
