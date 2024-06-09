package com.example.localguidebe.service.impl;

import com.example.localguidebe.converter.WardToWardDtoConverter;
import com.example.localguidebe.dto.WardDTO;
import com.example.localguidebe.repository.WardRepository;
import com.example.localguidebe.service.WardService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WardServiceImpl implements WardService {
  private WardRepository wardRepository;
  private WardToWardDtoConverter wardToWardDtoConverter;

  @Autowired
  public void setWardServiceImpl(
      WardRepository wardRepository, WardToWardDtoConverter wardToWardDtoConverter) {
    this.wardRepository = wardRepository;
    this.wardToWardDtoConverter = wardToWardDtoConverter;
  }

  @Override
  public List<String> getWardByDistrict(String name) {
    return wardRepository.getWardByDistrict(name);
  }

}
