package com.example.localguidebe.service.impl;

import com.example.localguidebe.converter.ProvinceToProvinceDtoConverter;
import com.example.localguidebe.dto.ProvinceDTO;
import com.example.localguidebe.repository.ProvinceRepository;
import com.example.localguidebe.service.ProvinceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProvinceServiceImpl implements ProvinceService {
    private ProvinceRepository provinceRepository;
    private ProvinceToProvinceDtoConverter provinceToProvinceDtoConverter;
    @Autowired
    public ProvinceServiceImpl(ProvinceRepository provinceRepository, ProvinceToProvinceDtoConverter provinceToProvinceDtoConverter){
        this.provinceRepository = provinceRepository;
        this.provinceToProvinceDtoConverter = provinceToProvinceDtoConverter;
    }

    public List<String> getProvinceByName(){
        return provinceRepository.getProvinceByName();
    }
}
