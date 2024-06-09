package com.example.localguidebe.converter;

import com.example.localguidebe.dto.LocationDTO;
import com.example.localguidebe.entity.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationToLocationDtoConverter {
  LocationDTO convert(Location location) {
    return new LocationDTO(
        location.getId(),
        location.getName(),
        location.getAddress(),
        location.getLatitude(),
        location.getLongitude());
  }
}
