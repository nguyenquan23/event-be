package com.example.localguidebe.converter;

import com.example.localguidebe.dto.DistrictDTO;
import com.example.localguidebe.entity.District;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class DistrictToDistrictDtoConverter {
    public final WardToWardDtoConverter wardToWardDtoConverter;
    public DistrictToDistrictDtoConverter(WardToWardDtoConverter wardToWardDtoConverter){
        this.wardToWardDtoConverter =wardToWardDtoConverter;

    }
    public DistrictDTO convert(District district){
        return new DistrictDTO(
                district.getCode(),
                district.getName(),
                district.getNameEn(),
                district.getFullName(),
                district.getFullNameEn(),
                district.getCodeName(),
                district.getWards().stream().map(wardToWardDtoConverter::convert).collect(Collectors.toList())
              
        );

    }

}
