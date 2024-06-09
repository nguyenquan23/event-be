package com.example.localguidebe.controller;

import com.example.localguidebe.security.service.CustomUserDetails;
import com.example.localguidebe.service.StatisticService;
import com.example.localguidebe.system.Result;
import com.example.localguidebe.utils.AuthUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/statistic")
public class StatisticController {
  private final StatisticService statisticService;

  public StatisticController(StatisticService statisticService) {
    this.statisticService = statisticService;
  }

  @GetMapping("/guides")
  public ResponseEntity<Result> getStatisticByGuides(
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "5") Integer limit,
      @RequestParam(required = false, defaultValue = "desc") String order) {
    try {
      return new ResponseEntity<>(
          new Result(
              false,
              HttpStatus.OK.value(),
              "get statistic of guide successfully",
              statisticService.getStatisticalByGuide(page, limit, order)),
          HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(
          new Result(false, HttpStatus.CONFLICT.value(), "Get statistics of failed guides", null),
          HttpStatus.CONFLICT);
    }
  }

  @GetMapping("/tours")
  public ResponseEntity<Result> getStatisticByTours(
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "5") Integer limit,
      @RequestParam(required = false, defaultValue = "desc") String order) {
    try {
      return new ResponseEntity<>(
          new Result(
              false,
              HttpStatus.OK.value(),
              "get statistic of tour successfully",
              statisticService.getStatisticalByTour(page, limit, order)),
          HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(
          new Result(false, HttpStatus.CONFLICT.value(), "Get statistics of failed tours", null),
          HttpStatus.CONFLICT);
    }
  }

  @GetMapping("/tour/{TourId}")
  public ResponseEntity<Result> getStatisticByPerTour(
      @PathVariable("TourId") Long tourId, Authentication authentication) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          try {
            return new ResponseEntity<>(
                new Result(
                    false,
                    HttpStatus.OK.value(),
                    "get statistic of per tour successfully",
                    statisticService.getStatisticByPerTour(tourId)),
                HttpStatus.OK);
          } catch (Exception e) {
            return new ResponseEntity<>(
                new Result(
                    false, HttpStatus.CONFLICT.value(), "Get statistics of failed per tour", null),
                HttpStatus.CONFLICT);
          }
        });
  }

  @GetMapping("/guide")
  public ResponseEntity<Result> getStatisticByPerGuide(Authentication authentication) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          try {
            return new ResponseEntity<>(
                new Result(
                    false,
                    HttpStatus.OK.value(),
                    "get statistic of per guide successfully",
                    statisticService.getStatisticByPerGuide(
                        ((CustomUserDetails) authentication.getPrincipal()).getId())),
                HttpStatus.OK);
          } catch (Exception e) {
            return new ResponseEntity<>(
                new Result(
                    false, HttpStatus.CONFLICT.value(), "Get statistics of failed per guide", null),
                HttpStatus.CONFLICT);
          }
        });
  }

  @GetMapping("/guide/tours")
  public ResponseEntity<Result> getStatisticOfToursByGuide(
      Authentication authentication,
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "5") Integer limit,
      @RequestParam(required = false, defaultValue = "desc") String order) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          try {
            return new ResponseEntity<>(
                new Result(
                    false,
                    HttpStatus.OK.value(),
                    "get statistic of tours by guide successfully",
                    statisticService.getStatisticOfToursByGuide(
                        ((CustomUserDetails) authentication.getPrincipal()).getId(),
                        page,
                        limit,
                        order)),
                HttpStatus.OK);
          } catch (Exception e) {
            return new ResponseEntity<>(
                new Result(
                    false,
                    HttpStatus.CONFLICT.value(),
                    "Get statistics of tours by guide failure",
                    null),
                HttpStatus.CONFLICT);
          }
        });
  }

  @GetMapping("/month/admin/{year}")
  public ResponseEntity<Result> getStatisticOfYearForAdmin(
      @PathVariable("year") int year, Authentication authentication) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          try {
            return new ResponseEntity<>(
                new Result(
                    false,
                    HttpStatus.OK.value(),
                    "get statistic of year for admin successfully",
                    statisticService.getStatisticByMonthForAdmin(year)),
                HttpStatus.OK);
          } catch (Exception e) {
            return new ResponseEntity<>(
                new Result(
                    false,
                    HttpStatus.CONFLICT.value(),
                    "Get statistics of year for admin failure",
                    null),
                HttpStatus.CONFLICT);
          }
        });
  }

  @GetMapping("/month/guide/{year}")
  public ResponseEntity<Result> getStatisticOfYearForGuide(
      @PathVariable("year") int year, Authentication authentication) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          try {
            return new ResponseEntity<>(
                new Result(
                    false,
                    HttpStatus.OK.value(),
                    "get statistic of year for guide successfully",
                    statisticService.getStatisticByMonthForGuide(
                        year, ((CustomUserDetails) authentication.getPrincipal()).getId())),
                HttpStatus.OK);
          } catch (Exception e) {
            return new ResponseEntity<>(
                new Result(
                    false,
                    HttpStatus.CONFLICT.value(),
                    "Get statistics of year for guide failure",
                    null),
                HttpStatus.CONFLICT);
          }
        });
  }

  @GetMapping("/popular")
  public ResponseEntity<Result> getTopRevenue(Authentication authentication) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          try {
            return new ResponseEntity<>(
                new Result(
                    false,
                    HttpStatus.OK.value(),
                    "Get the 5 tour guides with the highest revenue successfully",
                    statisticService.getTopRevenue()),
                HttpStatus.OK);
          } catch (Exception e) {
            return new ResponseEntity<>(
                new Result(
                    false,
                    HttpStatus.CONFLICT.value(),
                    "failed to get the 5 tour guides with the highest revenue successfully",
                    null),
                HttpStatus.CONFLICT);
          }
        });
  }
}
