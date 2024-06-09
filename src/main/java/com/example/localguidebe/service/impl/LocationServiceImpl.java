package com.example.localguidebe.service.impl;

import com.example.localguidebe.entity.Location;
import com.example.localguidebe.repository.LocationRepository;
import com.example.localguidebe.service.LocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LocationServiceImpl implements LocationService {
    private final LocationRepository locationRepository;

    @Autowired
    public LocationServiceImpl(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    @Override
    public Location findById(Long id) {
        return locationRepository.findById(id).orElseThrow();
    }

    @Override
    public Location save(Location location) {
        return locationRepository.save(location);
    }
}
