package com.example.localguidebe.converter;

import com.example.localguidebe.dto.ProvinceDTO;
import com.example.localguidebe.entity.Province;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class ProvinceToProvinceDtoConverter {
  private final DistrictToDistrictDtoConverter districtToDistrictDtoConverter;
  private ProvinceToProvinceDtoConverter( DistrictToDistrictDtoConverter districtToDistrictDtoConverter){
    this.districtToDistrictDtoConverter = districtToDistrictDtoConverter;

  }
  public ProvinceDTO convert(Province province) {
    return new ProvinceDTO(
        province.getCode(),
        province.getName(),
        province.getNameEn(),
        province.getFullName(),
        province.getFullNameEn(),
        province.getCodeName(),
        province.getDistricts().stream().map(districtToDistrictDtoConverter::convert).collect(Collectors.toList()));
  }
}
