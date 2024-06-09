package com.example.localguidebe.service.impl;

import com.example.localguidebe.converter.BusyScheduleToBusyScheduleDtoConverter;
import com.example.localguidebe.dto.BusyScheduleDTO;
import com.example.localguidebe.entity.Booking;
import com.example.localguidebe.entity.BusySchedule;
import com.example.localguidebe.entity.Tour;
import com.example.localguidebe.entity.User;
import com.example.localguidebe.enums.TypeBusyDayEnum;
import com.example.localguidebe.repository.BookingRepository;
import com.example.localguidebe.repository.BusyScheduleRepository;
import com.example.localguidebe.repository.TourRepository;
import com.example.localguidebe.repository.UserRepository;
import com.example.localguidebe.service.BusyScheduleService;
import com.example.localguidebe.service.TourService;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BusyScheduleServiceImpl implements BusyScheduleService {
  Logger logger = LoggerFactory.getLogger(BusyScheduleServiceImpl.class);
  private final BusyScheduleRepository busyScheduleRepository;
  private final BusyScheduleToBusyScheduleDtoConverter busyScheduleToBusyScheduleDtoConverter;
  private final UserRepository userRepository;

  private final TourRepository tourRepository;
  private final BookingRepository bookingRepository;

  private final TourService tourService;

  @Autowired
  public BusyScheduleServiceImpl(
      BusyScheduleRepository busyScheduleRepository,
      UserRepository userRepository,
      BusyScheduleToBusyScheduleDtoConverter busyScheduleToBusyScheduleDtoConverter,
      TourRepository tourRepository,
      BookingRepository bookingRepository,
      TourService tourService) {
    this.busyScheduleRepository = busyScheduleRepository;
    this.userRepository = userRepository;
    this.busyScheduleToBusyScheduleDtoConverter = busyScheduleToBusyScheduleDtoConverter;
    this.tourRepository = tourRepository;
    this.bookingRepository = bookingRepository;
    this.tourService = tourService;
  }

  @Override
  public List<BusyScheduleDTO> InsertAndUpdateBusyDates(
      List<LocalDateTime> busyDates, String email) {
    List<BusySchedule> busySchedules = new ArrayList<>();
    User guide = userRepository.findUserByEmail(email);

    busyDates.forEach(
        busyDate -> {
          BusySchedule busySchedule = new BusySchedule();
          busySchedule.setBusyDate(busyDate);
          busySchedule.setGuide(guide);
          busySchedule.setTypeBusyDay(TypeBusyDayEnum.DATE_SELECTED_BY_GUIDE);
          busySchedules.add(busySchedule);
        });
    busyScheduleRepository.deleteAll(busyScheduleRepository.getBusySchedules());
    busyScheduleRepository.saveAll(busySchedules);

    return busyScheduleRepository.findAllByGuideId(guide.getId()).stream()
        .map(busyScheduleToBusyScheduleDtoConverter::convert)
        .collect(Collectors.toList());
  }

  @Override
  public List<BusyScheduleDTO> getBusyScheduleByGuide(String email) {
    User guide = userRepository.findUserByEmail(email);
    return busyScheduleRepository.findAllByGuideId(guide.getId()).stream()
        .map(busyScheduleToBusyScheduleDtoConverter::convert)
        .collect(Collectors.toList());
  }

  @Override
  public Set<LocalDate> getBusyDateByTour(Long tourId) {
    Tour tour = tourRepository.findById(tourId).orElseThrow();
    String guideEmail = tour.getGuide().getEmail();

    List<BusyScheduleDTO> busySchedules = getBusyScheduleByGuide(guideEmail);
    List<LocalDate> busyDates =
        busySchedules.stream()
            .map(BusyScheduleDTO::busyDate)
            .map(LocalDateTime::toLocalDate)
            .collect(Collectors.toList());

    List<LocalDate> busyDatesByHours =
        busySchedules.stream()
            .filter(
                schedule -> schedule.typeBusyDayEnum().equals(TypeBusyDayEnum.BOOKED_DAY_BY_HOURS))
            .map(BusyScheduleDTO::busyDate)
            .map(LocalDateTime::toLocalDate)
            .collect(Collectors.toList());

    Set<LocalDate> updatedBusyDates = new HashSet<>();

    if (tour.getUnit().equals("day(s)")) {
      updatedBusyDates.addAll(busyDates);
      for (int count = 1; count < tour.getDuration(); count++) {
        for (LocalDate busyDate : busyDates) {
          updatedBusyDates.add(busyDate.minusDays(count));
        }
      }
    } else {
      if (!busyDatesByHours.isEmpty()) {
        busyDates.removeAll(busyDatesByHours);
      }
      updatedBusyDates.addAll(busyDates);
      busyDatesByHours.stream()
          .filter(
              busyDatesByHour ->
                  tourService.getTourStartTimeAvailable(tourId, busyDatesByHour).isEmpty())
          .forEach(updatedBusyDates::add);
    }
    return updatedBusyDates;
  }

  @Override
  public List<BusyScheduleDTO> getBusySchedulesAndPreBookedSchedules(String email) {
    return busyScheduleRepository.findAll().stream()
        .map(busyScheduleToBusyScheduleDtoConverter::convert)
        .collect(Collectors.toList());
  }

  /**
   * @param newDate: new value of start date include start time , if null, use for delete booking
   * @param booking: entity before update start date
   * @return boolean
   */
  @Transactional
  @Override
  public boolean updateBusyScheduleBeforeUpdateOrDeleteBooking(
      LocalDateTime newDate, Booking booking) {
    User guide = booking.getTour().getGuide();
    List<BusySchedule> newBusySchedules = new ArrayList<>();

    int count = 0;
    if (booking.getTour().getUnit().equals("day(s)")) {
      while (count < booking.getTour().getDuration()) {
        BusySchedule oldBusySchedule =
            busyScheduleRepository.findBusyScheduleByBusyDateAndGuideAndTypeBusyDay(
                booking.getStartDate().plusDays(count), guide, TypeBusyDayEnum.BOOKED_DAY_BY_DAYS);
        busyScheduleRepository.delete(oldBusySchedule);
        if (newDate != null) {
          newBusySchedules.add(
              BusySchedule.builder()
                  .busyDate(newDate.plusDays(count))
                  .typeBusyDay(TypeBusyDayEnum.BOOKED_DAY_BY_DAYS)
                  .guide(guide)
                  .build());
        }

        count++;
      }
    } else {
      if (newDate != null) {
        BusySchedule oldBusySchedule =
            busyScheduleRepository.findBusyScheduleByBusyDateAndGuideAndTypeBusyDay(
                booking.getStartDate(), guide, TypeBusyDayEnum.BOOKED_DAY_BY_HOURS);
        busyScheduleRepository.delete(oldBusySchedule);
        newBusySchedules.add(
            BusySchedule.builder()
                .busyDate(newDate)
                .typeBusyDay(TypeBusyDayEnum.BOOKED_DAY_BY_HOURS)
                .guide(guide)
                .build());
      }
    }
    guide.getBusySchedules().addAll(newBusySchedules);
    userRepository.save(guide);
    return true;
  }
}
