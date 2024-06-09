package com.example.localguidebe.service;

import com.example.localguidebe.dto.DistrictDTO;

import java.util.List;

public interface DistrictService {
    List<String> getDistrictByProvince(String districtName);
}
