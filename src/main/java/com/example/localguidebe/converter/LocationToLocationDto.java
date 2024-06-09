package com.example.localguidebe.converter;

import com.example.localguidebe.dto.LocationDTO;
import com.example.localguidebe.entity.Location;
import org.springframework.stereotype.Component;

@Component
public class LocationToLocationDto {
    public LocationDTO convert(Location source) {
        return new LocationDTO(source.getId(),
                source.getName(),
                source.getAddress(),
                source.getLatitude(),
                source.getLongitude());
    }
}
