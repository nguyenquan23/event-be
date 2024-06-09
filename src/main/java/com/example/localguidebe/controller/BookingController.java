package com.example.localguidebe.controller;

import com.example.localguidebe.security.service.CustomUserDetails;
import com.example.localguidebe.service.BookingService;
import com.example.localguidebe.system.Result;
import com.example.localguidebe.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/bookings")
public class BookingController {
  private BookingService bookingService;

  @Autowired
  public BookingController(BookingService bookingService) {
    this.bookingService = bookingService;
  }

  @GetMapping("/history")
  public ResponseEntity<Result> getBookingHistory(Authentication authentication) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          String email = ((CustomUserDetails) authentication.getPrincipal()).getEmail();
          try {
            return new ResponseEntity<>(
                new Result(
                    true,
                    HttpStatus.OK.value(),
                    "Successfully get the booking history",
                    bookingService.getBookingHistory(email)),
                HttpStatus.OK);
          } catch (Exception e) {
            return new ResponseEntity<>(
                new Result(
                    false, HttpStatus.CONFLICT.value(), "failed get the booking history", null),
                HttpStatus.CONFLICT);
          }
        });
  }

  @GetMapping("/province")
  public ResponseEntity<Result> findTotalBookingsByCityProvince() {
    try {
      return new ResponseEntity<>(
          new Result(
              true,
              HttpStatus.OK.value(),
              "Successfully get the popular province",
              bookingService.FindForSuggestedTours()),
          HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(
          new Result(false, HttpStatus.CONFLICT.value(), "failed get popular province", null),
          HttpStatus.CONFLICT);
    }
  }
}
