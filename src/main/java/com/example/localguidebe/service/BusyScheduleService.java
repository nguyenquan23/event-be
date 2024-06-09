package com.example.localguidebe.service;

import com.example.localguidebe.dto.BusyScheduleDTO;
import com.example.localguidebe.entity.Booking;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

public interface BusyScheduleService {
  List<BusyScheduleDTO> InsertAndUpdateBusyDates(List<LocalDateTime> busyDates, String email);

  List<BusyScheduleDTO> getBusyScheduleByGuide(String email);

  Set<LocalDate> getBusyDateByTour(Long tourId);

  List<BusyScheduleDTO> getBusySchedulesAndPreBookedSchedules(String email);

  boolean updateBusyScheduleBeforeUpdateOrDeleteBooking(LocalDateTime newDate, Booking booking);
}
