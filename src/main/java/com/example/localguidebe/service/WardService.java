package com.example.localguidebe.service;

import com.example.localguidebe.dto.WardDTO;

import java.util.List;

public interface WardService {

  List<String> getWardByDistrict(String districtName);
}
