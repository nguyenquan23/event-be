package com.example.localguidebe.converter;

import com.example.localguidebe.dto.WardDTO;
import com.example.localguidebe.entity.Ward;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class WardToWardDtoConverter {



  public WardDTO convert(Ward ward) {
    return new WardDTO(
        ward.getCode() != null ? ward.getCode():null,
        ward.getName() != null ?ward.getName() : null,
        ward.getNameEn() != null ? ward.getNameEn() :null,
        ward.getFullName(),
        ward.getFullNameEn(),
        ward.getCodeName()
  
    );
  }
}
