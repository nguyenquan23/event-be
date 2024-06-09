package com.example.localguidebe.service.impl;

import com.example.localguidebe.repository.TourStartTimeRepository;
import com.example.localguidebe.service.TourStartTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TourStartTimeServiceImpl implements TourStartTimeService {
    private final TourStartTimeRepository tourStartTimeRepository;

    @Autowired
    public TourStartTimeServiceImpl(TourStartTimeRepository tourStartTimeRepository) {
        this.tourStartTimeRepository = tourStartTimeRepository;
    }

    @Override
    public void deleteById(Integer id) {
        tourStartTimeRepository.deleteById(id);
    }
}
