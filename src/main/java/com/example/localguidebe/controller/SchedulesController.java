package com.example.localguidebe.controller;

import com.example.localguidebe.security.service.CustomUserDetails;
import com.example.localguidebe.service.BusyScheduleService;
import com.example.localguidebe.system.Result;
import com.example.localguidebe.utils.AuthUtils;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/schedules")
public class SchedulesController {
  private final BusyScheduleService busyScheduleService;

  @Autowired
  public SchedulesController(BusyScheduleService busyScheduleService) {
    this.busyScheduleService = busyScheduleService;
  }

  @PostMapping("/busy")
  public ResponseEntity<Result> InsertAndUpdateBusyDates(
      @RequestBody List<LocalDateTime> busyDates, Authentication authentication) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          try {
            return new ResponseEntity<>(
                new Result(
                    true,
                    HttpStatus.OK.value(),
                    "Added busy day list successfully",
                    busyScheduleService.InsertAndUpdateBusyDates(
                        busyDates, ((CustomUserDetails) authentication.getPrincipal()).getEmail())),
                HttpStatus.OK);
          } catch (Exception e) {
            return new ResponseEntity<>(
                new Result(
                    false, HttpStatus.CONFLICT.value(), "Add list of failed busy days", null),
                HttpStatus.CONFLICT);
          }
        });
  }

  @GetMapping("/busy")
  public ResponseEntity<Result> getBusyDates(Authentication authentication) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          try {
            return new ResponseEntity<>(
                new Result(
                    true,
                    HttpStatus.OK.value(),
                    "Get a list of busy days successfully",
                    busyScheduleService.getBusyScheduleByGuide(
                        ((CustomUserDetails) authentication.getPrincipal()).getEmail())),
                HttpStatus.OK);
          } catch (Exception e) {
            return new ResponseEntity<>(
                new Result(
                    false, HttpStatus.CONFLICT.value(), "Get a list of failed busy days", null),
                HttpStatus.CONFLICT);
          }
        });
  }

  @GetMapping("/busy/tour/{tourId}")
  public ResponseEntity<Result> getBusyDateByTour(@PathVariable("tourId") Long tourId) {
    try {
      return new ResponseEntity<>(
          new Result(
              false,
              HttpStatus.OK.value(),
              "Successfully get the tour guide's busy schedule",
              busyScheduleService.getBusyDateByTour(tourId)),
          HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(
          new Result(
              false,
              HttpStatus.CONFLICT.value(),
              "Failed to get the tour guide's busy schedule",
              null),
          HttpStatus.CONFLICT);
    }
  }

  @GetMapping("/busy/guide")
  public ResponseEntity<Result> getBusySchedulesAndPreBookedSchedules(
      Authentication authentication) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          try {
            return new ResponseEntity<>(
                new Result(
                    true,
                    HttpStatus.OK.value(),
                    "Get a list of busy schedules and pre booked schedules successfully",
                    busyScheduleService.getBusyScheduleByGuide(
                        ((CustomUserDetails) authentication.getPrincipal()).getEmail())),
                HttpStatus.OK);
          } catch (Exception e) {
            return new ResponseEntity<>(
                new Result(
                    false,
                    HttpStatus.CONFLICT.value(),
                    "Get a list of of busy schedules and pre booked schedules",
                    null),
                HttpStatus.CONFLICT);
          }
        });
     }

}
