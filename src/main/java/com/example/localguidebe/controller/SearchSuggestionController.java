package com.example.localguidebe.controller;

import com.example.localguidebe.service.TourService;
import com.example.localguidebe.service.UserService;
import com.example.localguidebe.system.Result;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/search-suggestion")
public class SearchSuggestionController {
  private final UserService userService;
  private final TourService tourService;

  public SearchSuggestionController(UserService userService, TourService tourService) {
    this.userService = userService;
    this.tourService = tourService;
  }

  @GetMapping("")
  public ResponseEntity<Result> searchSuggestion(
      @RequestParam() String searchType, @RequestParam() String searchValue) {
    if (searchType.equals("guide")) {
      return new ResponseEntity<>(
          new Result(
              true,
              HttpStatus.OK.value(),
              "Value founded",
              userService.getGuidesAndGuideAddresses(searchValue)),
          HttpStatus.OK);
    } else if (searchType.equals("tour")) {
      return new ResponseEntity<>(
          new Result(
              true,
              HttpStatus.OK.value(),
              "Value founded",
              tourService.getTourAndTourLocations(searchValue)),
          HttpStatus.OK);
    }
    return new ResponseEntity<>(
        new Result(true, HttpStatus.NO_CONTENT.value(), "Not found", null), HttpStatus.NO_CONTENT);
  }
}
