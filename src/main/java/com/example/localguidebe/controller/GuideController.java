package com.example.localguidebe.controller;

import com.example.localguidebe.converter.UserToGuideDtoConverter;
import com.example.localguidebe.dto.responsedto.SearchGuideDTO;
import com.example.localguidebe.entity.User;
import com.example.localguidebe.security.service.CustomUserDetails;
import com.example.localguidebe.service.BookingService;
import com.example.localguidebe.service.UserService;
import com.example.localguidebe.system.Result;
import com.example.localguidebe.utils.AuthUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/guides")
public class GuideController {

  private final UserService userService;
  private final UserToGuideDtoConverter userToGuideDtoConverter;
  private final BookingService bookingService;

  @Autowired
  public GuideController(
      UserService userService,
      UserToGuideDtoConverter userToGuideDtoConverter,
      BookingService bookingService) {
    this.userService = userService;
    this.userToGuideDtoConverter = userToGuideDtoConverter;
    this.bookingService = bookingService;
  }

  @GetMapping("/search")
  public ResponseEntity<Result> search(
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "5") Integer limit,
      @RequestParam(required = false, defaultValue = "overallRating") String sortBy,
      @RequestParam(required = false, defaultValue = "desc") String order,
      @RequestParam(required = false, defaultValue = "") Double ratingFilter,
      @RequestParam(required = false, defaultValue = "") String searchValue) {
    Page<User> guides =
        userService.getGuides(page, limit, sortBy, order, ratingFilter, searchValue);

    return ResponseEntity.status(HttpStatus.OK)
        .body(
            new Result(
                true,
                HttpStatus.OK.value(),
                "All guide",
                new SearchGuideDTO(
                    guides.stream().map(userToGuideDtoConverter::convert).toList(),
                    guides.getTotalPages(),
                    (int) guides.getTotalElements())));
  }

  @GetMapping("/{id}")
  public ResponseEntity<Result> getGuideById(@PathVariable("id") Long id) {
    return userService
        .findById(id)
        .map(
            user ->
                ResponseEntity.status(HttpStatus.OK)
                    .body(
                        new Result(
                            true,
                            HttpStatus.OK.value(),
                            "Find guide successfully",
                            userToGuideDtoConverter.convert(user))))
        .orElseGet(
            () ->
                ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new Result(true, HttpStatus.NOT_FOUND.value(), "Not found guide")));
  }

  @GetMapping("/bookings")
  public ResponseEntity<Result> getBookingsOfGuide(Authentication authentication) {
    try {
      return AuthUtils.checkAuthentication(
          authentication,
          () -> {
            String email = ((CustomUserDetails) authentication.getPrincipal()).getEmail();
            return ResponseEntity.status(HttpStatus.OK)
                .body(
                    new Result(
                        true,
                        HttpStatus.OK.value(),
                        "Get bookings of guide successfully",
                        bookingService.getBookingsOfGuide(email)));
          });
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              new Result(
                  false,
                  HttpStatus.INTERNAL_SERVER_ERROR.value(),
                  "Get bookings of guide false: " + e.getMessage()));
    }
  }
}
