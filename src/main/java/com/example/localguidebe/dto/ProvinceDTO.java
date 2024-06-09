package com.example.localguidebe.dto;

import com.example.localguidebe.entity.District;
import java.util.List;

public record ProvinceDTO(
    String code,
    String name,
    String nameEn,
    String fullName,
    String fullNameEn,
    String codeName,
    List<DistrictDTO> districtDTOS) {}
