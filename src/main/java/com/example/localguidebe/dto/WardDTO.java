package com.example.localguidebe.dto;

import com.example.localguidebe.entity.AdministrativeUnit;
import com.example.localguidebe.entity.District;

public record WardDTO(
    String code,
    String name,
    String nameEn,
    String fullName,
    String fullNameEn,
    String codeName
 ) {}
