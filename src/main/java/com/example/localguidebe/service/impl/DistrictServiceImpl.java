package com.example.localguidebe.service.impl;

import com.example.localguidebe.converter.DistrictToDistrictDtoConverter;
import com.example.localguidebe.dto.DistrictDTO;
import com.example.localguidebe.repository.DistrictRepository;
import com.example.localguidebe.service.DistrictService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DistrictServiceImpl implements DistrictService {
  private DistrictToDistrictDtoConverter districtToDistrictDtoConverter;
  private DistrictRepository districtRepository;

  @Autowired
  public void setDistrictServiceImpl(
      DistrictRepository districtRepository,
      DistrictToDistrictDtoConverter districtToDistrictDtoConverter) {
    this.districtRepository = districtRepository;
    this.districtToDistrictDtoConverter = districtToDistrictDtoConverter;
  }


  public List<String> getDistrictByProvince(String provinceName){
    return districtRepository.getDistrictByProvince(provinceName);
  }
}
