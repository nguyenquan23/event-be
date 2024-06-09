package com.example.localguidebe.dto;

import com.example.localguidebe.enums.RolesEnum;
import java.time.LocalDateTime;

public record UserDTO(
    Long id,
    String email,
    String fullName,
    LocalDateTime dateOfBirth,
    String phone,
    String address,
    RolesEnum role) {}
