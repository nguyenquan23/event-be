package com.example.localguidebe.controller;

import com.example.localguidebe.converter.ReviewToReviewDtoConverter;
import com.example.localguidebe.dto.requestdto.ReviewRequestDTO;
import com.example.localguidebe.entity.Review;
import com.example.localguidebe.entity.User;
import com.example.localguidebe.security.service.CustomUserDetails;
import com.example.localguidebe.service.ReviewService;
import com.example.localguidebe.service.TourService;
import com.example.localguidebe.service.UserService;
import com.example.localguidebe.system.Result;
import com.example.localguidebe.utils.AuthUtils;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reviews")
public class ReviewController {
  private final ReviewService reviewService;
  private final UserService userService;
  private final ReviewToReviewDtoConverter reviewToReviewDtoConverter;
  private final TourService tourService;

  public ReviewController(
      ReviewService reviewService,
      UserService userService,
      ReviewToReviewDtoConverter reviewToReviewDtoConverter,
      TourService tourService) {
    this.tourService = tourService;
    this.reviewService = reviewService;
    this.userService = userService;
    this.reviewToReviewDtoConverter = reviewToReviewDtoConverter;
  }

  @PostMapping("guide-reviews/{guideId}")
  public ResponseEntity<Result> addReviewForGuide(
      Authentication authentication,
      @RequestBody ReviewRequestDTO reviewRequestDTO,
      @PathVariable("guideId") Long guideId) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          String travelerEmail = ((CustomUserDetails) authentication.getPrincipal()).getEmail();
          User traveler = userService.findUserByEmail(travelerEmail);
          if (!userService.isTravelerCanAddReviewForGuide(traveler, guideId)) {
            return ResponseEntity.status(HttpStatus.OK)
                .body(
                    new Result(
                        false, HttpStatus.OK.value(), "You can't add review for this guide"));
          }
          return ResponseEntity.status(HttpStatus.OK)
              .body(
                  new Result(
                      true,
                      HttpStatus.OK.value(),
                      "Thank you for giving feedback.",
                      reviewToReviewDtoConverter.convert(
                          reviewService.addReviewForGuide(reviewRequestDTO, guideId, traveler))));
        });
  }

  @PostMapping("tour-reviews/{tourId}")
  public ResponseEntity<Result> addReviewForTour(
      Authentication authentication,
      @RequestBody ReviewRequestDTO reviewRequestDTO,
      @PathVariable("tourId") Long tourId) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          if (!tourService.checkBookingByTraveler(
              tourId, ((CustomUserDetails) authentication.getPrincipal()).getEmail())) {
            return new ResponseEntity<>(
                new Result(
                    true,
                    HttpStatus.CONFLICT.value(),
                    "You haven't booked a tour yet so you can't review it",
                    null),
                HttpStatus.CONFLICT);
          } else if (!tourService.checkExistingReviewsByTraveler(
              ((CustomUserDetails) authentication.getPrincipal()).getId(), tourId)) {
            return new ResponseEntity<>(
                new Result(
                    true, HttpStatus.CONFLICT.value(), "You have already rated this tour", null),
                HttpStatus.CONFLICT);
          } else {
            try {
              return new ResponseEntity<>(
                  new Result(
                      true,
                      HttpStatus.OK.value(),
                      "Added review for tour successfully",
                      reviewService.addReviewForTour(
                          reviewRequestDTO,
                          tourId,
                          ((CustomUserDetails) authentication.getPrincipal()).getEmail())),
                  HttpStatus.OK);
            } catch (Exception e) {
              return new ResponseEntity<>(
                  new Result(
                      false, HttpStatus.CONFLICT.value(), "Added review for failed tour", null),
                  HttpStatus.CONFLICT);
            }
          }
        });
  }

  @GetMapping("tour-reviews/{tourId}")
  public ResponseEntity<Result> getReviewForTour(@PathVariable("tourId") Long tourId) {
    try {
      return new ResponseEntity<>(
          new Result(
              false,
              HttpStatus.OK.value(),
              "Get the list of reviews of successful tours",
              reviewService.getReviewForTour(tourId)),
          HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(
          new Result(
              false, HttpStatus.CONFLICT.value(), "Get the review list of failed tours", null),
          HttpStatus.CONFLICT);
    }
  }

  @GetMapping("guide-reviews/filter/{guideId}")
  public ResponseEntity<Result> getReviewsForGuide(
      @PathVariable("guideId") Long guideId,
      @RequestParam(required = false, defaultValue = "") List<Integer> ratings,
      @RequestParam(required = false, defaultValue = "Most recent") String sortBy) {
    try {
      List<Review> reviews = reviewService.getReviewsOfGuide(guideId, ratings, sortBy);
      if (reviews.isEmpty()) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(new Result(true, HttpStatus.OK.value(), "No review for this condition"));
      }
      return ResponseEntity.status(HttpStatus.OK)
          .body(
              new Result(
                  true,
                  HttpStatus.OK.value(),
                  "Thank you for giving feedback.",
                  reviews.stream().map(reviewToReviewDtoConverter::convert)));
    } catch (Exception e) {
      return new ResponseEntity<>(
          new Result(
              false, HttpStatus.CONFLICT.value(), "Get the failure comment list for guide", null),
          HttpStatus.CONFLICT);
    }
  }

  @PutMapping("tour-reviews/{reviewId}")
  public ResponseEntity<Result> editReviewForTour(
      @PathVariable("reviewId") Long reviewId,
      Authentication authentication,
      @RequestBody ReviewRequestDTO reviewRequestDTO) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          if (!reviewService.checkReviewByTraveler(
              reviewId, ((CustomUserDetails) authentication.getPrincipal()).getEmail())) {
            return new ResponseEntity<>(
                new Result(
                    true,
                    HttpStatus.CONFLICT.value(),
                    "You haven't booked a tour yet so you can't review it",
                    null),
                HttpStatus.CONFLICT);
          } else {
            try {
              return new ResponseEntity<>(
                  new Result(
                      true,
                      HttpStatus.OK.value(),
                      "Successfully updated the review for the tour",
                      reviewService.editReviewForTour(reviewId, reviewRequestDTO)),
                  HttpStatus.OK);
            } catch (Exception e) {
              return new ResponseEntity<>(
                  new Result(
                      false, HttpStatus.CONFLICT.value(), "failed update review for tour", null),
                  HttpStatus.CONFLICT);
            }
          }
        });
  }

  @DeleteMapping("tour-reviews/{reviewId}")
  public ResponseEntity<Result> deleteReviewForTour(
      @PathVariable("reviewId") Long reviewId, Authentication authentication) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          if (!reviewService.checkReviewByTraveler(
              reviewId, ((CustomUserDetails) authentication.getPrincipal()).getEmail())) {
            return new ResponseEntity<>(
                new Result(
                    true,
                    HttpStatus.CONFLICT.value(),
                    "You haven't booked a tour yet so you can't review it",
                    null),
                HttpStatus.CONFLICT);
          } else {
            try {
              return new ResponseEntity<>(
                  new Result(
                      true,
                      HttpStatus.OK.value(),
                      "Successfully removed review for tour",
                      reviewService.deleteReviewForTour(reviewId)),
                  HttpStatus.OK);
            } catch (Exception e) {
              return new ResponseEntity<>(
                  new Result(
                      false,
                      HttpStatus.CONFLICT.value(),
                      "Reviews for the tour cannot be removed",
                      null),
                  HttpStatus.CONFLICT);
            }
          }
        });
  }

  @DeleteMapping("guide-reviews/{reviewId}")
  public ResponseEntity<Result> deleteReviewForGuide(
      Authentication authentication, @PathVariable("reviewId") Long reviewId) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          String travelerEmail = ((CustomUserDetails) authentication.getPrincipal()).getEmail();
          User traveler = userService.findUserByEmail(travelerEmail);
          if (!reviewService.deleteReviewForGuide(traveler, reviewId)) {
            return ResponseEntity.status(HttpStatus.OK)
                .body(
                    new Result(
                        false, HttpStatus.OK.value(), "You can't not delete this review review"));
          }
          return ResponseEntity.status(HttpStatus.OK)
              .body(new Result(true, HttpStatus.OK.value(), "Delete review successful."));
        });
  }

  @PutMapping("guide-reviews/{reviewId}")
  public ResponseEntity<Result> editReviewForGuide(
      @PathVariable("reviewId") Long reviewId,
      Authentication authentication,
      @RequestBody ReviewRequestDTO reviewRequestDTO) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          String travelerEmail = ((CustomUserDetails) authentication.getPrincipal()).getEmail();
          User traveler = userService.findUserByEmail(travelerEmail);
          if (!reviewService.updateReviewForGuide(reviewRequestDTO, traveler, reviewId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(
                    new Result(
                        false, HttpStatus.CONFLICT.value(), "You can't not update this review"));
          }
          return ResponseEntity.status(HttpStatus.OK)
              .body(new Result(true, HttpStatus.OK.value(), "Update review successful."));
        });
  }

  @GetMapping("/tour-reviews/filter/{tourId}")
  public ResponseEntity<Result> getTourByFilter(
      @PathVariable Long tourId,
      @RequestParam(required = false, defaultValue = "") List<Integer> ratings,
      @RequestParam(required = false, defaultValue = "Most recent") String sortBy) {
    try {
      return new ResponseEntity<>(
          new Result(
              true,
              HttpStatus.OK.value(),
              "filter the comment list for tour successfully",
              tourService.filterReviewForTour(ratings, tourId, sortBy)),
          HttpStatus.OK);
    } catch (Exception e) {
      return new ResponseEntity<>(
          new Result(
              false, HttpStatus.CONFLICT.value(), "filter the failure comment list for tour", null),
          HttpStatus.CONFLICT);
    }
  }
}
