package com.example.localguidebe.converter;

import com.example.localguidebe.dto.BusyScheduleDTO;
import com.example.localguidebe.entity.BusySchedule;
import org.springframework.stereotype.Component;

@Component
public class BusyScheduleToBusyScheduleDtoConverter {
  public BusyScheduleDTO convert(BusySchedule busySchedule) {
    return new BusyScheduleDTO(
        busySchedule.getId(), busySchedule.getBusyDate(), busySchedule.getTypeBusyDay());
  }
}
