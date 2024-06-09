package com.example.localguidebe.service;

import com.example.localguidebe.entity.Location;

public interface LocationService {
    Location findById(Long id);
    Location save(Location location);
}
