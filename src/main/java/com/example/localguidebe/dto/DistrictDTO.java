package com.example.localguidebe.dto;

import com.example.localguidebe.entity.AdministrativeUnit;
import com.example.localguidebe.entity.Province;
import com.example.localguidebe.entity.Ward;
import jakarta.persistence.*;
import java.util.List;

public record DistrictDTO(
    String code,
    String name,
    String nameEn,
    String fullName,
    String fullNameEn,
    String codeName,
    List<WardDTO> wardDTOS
  ) {}
