package com.example.localguidebe.dto.requestdto;

import java.time.LocalDateTime;

public record UpdatePersonalInformationDTO(
    String fullName,
    LocalDateTime dateOfBirth,
    String phone,
    String address,
    String biography,
    String credential) {}
