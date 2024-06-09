package com.example.localguidebe.converter;

import com.example.localguidebe.dto.TourStartTimeDTO;
import com.example.localguidebe.entity.TourStartTime;
import org.springframework.stereotype.Component;

@Component
public class TourStartTimeToTourStartTimeDtoConverter {
    public TourStartTimeDTO convert (TourStartTime source) {
        return new TourStartTimeDTO(source.getId(), source.getStartTime());
    }
}
