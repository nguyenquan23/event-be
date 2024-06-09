package com.example.localguidebe.controller;

import com.example.localguidebe.converter.TravelerRequestToTravelerRequestDtoConverter;
import com.example.localguidebe.dto.requestdto.AddTravelerRequestDTO;
import com.example.localguidebe.dto.requestdto.UpdateTravelerRequestDTO;
import com.example.localguidebe.entity.TravelerRequest;
import com.example.localguidebe.security.service.CustomUserDetails;
import com.example.localguidebe.service.TravelerRequestService;
import com.example.localguidebe.system.Result;
import com.example.localguidebe.utils.AuthUtils;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/traveler-requests")
public class TravelerRequestController {
  private final TravelerRequestService travelerRequestService;
  private final TravelerRequestToTravelerRequestDtoConverter
      travelerRequestToTravelerRequestDtoConverter;

  public TravelerRequestController(
      TravelerRequestService travelerRequestService,
      TravelerRequestToTravelerRequestDtoConverter travelerRequestToTravelerRequestDtoConverter) {
    this.travelerRequestService = travelerRequestService;
    this.travelerRequestToTravelerRequestDtoConverter =
        travelerRequestToTravelerRequestDtoConverter;
  }

  @PostMapping("")
  public ResponseEntity<Result> addTravelerRequest(
      Authentication authentication, @RequestBody AddTravelerRequestDTO addTravelerRequestDTO) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          String email = ((CustomUserDetails) authentication.getPrincipal()).getEmail();
          TravelerRequest travelerRequest =
              travelerRequestService.addTravelerRequest(email, addTravelerRequestDTO);
          if (travelerRequest == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(
                    new Result(
                        false,
                        HttpStatus.NOT_ACCEPTABLE.value(),
                        "You have a PENDING request for this guide, consider removing the previous before add."));
          }
          return ResponseEntity.status(HttpStatus.OK)
              .body(
                  new Result(
                      true,
                      HttpStatus.OK.value(),
                      "Your request will be processed soon.",
                      travelerRequestToTravelerRequestDtoConverter.convert(travelerRequest)));
        });
  }

  @GetMapping("/{travelerRequestId}")
  public ResponseEntity<Result> getTravelerRequestById(
      @PathVariable("travelerRequestId") Long travelerRequestId) {
    try {
      TravelerRequest travelerRequest =
          travelerRequestService.findTravelerRequestById(travelerRequestId);
      if (travelerRequest == null) {
        return ResponseEntity.status(HttpStatus.OK)
            .body(new Result(true, HttpStatus.OK.value(), "Not found traveler request"));
      }
      return ResponseEntity.status(HttpStatus.OK)
          .body(
              new Result(
                  true,
                  HttpStatus.OK.value(),
                  "Get traveler request successfully",
                  travelerRequestToTravelerRequestDtoConverter.convert(travelerRequest)));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              new Result(
                  true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Get traveler request failed"));
    }
  }

  @GetMapping("")
  public ResponseEntity<Result> getTravelerRequests(Authentication authentication) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          String email = ((CustomUserDetails) authentication.getPrincipal()).getEmail();
          List<TravelerRequest> travelerRequests =
              travelerRequestService.getTravelerRequests(email);
          return ResponseEntity.status(HttpStatus.OK)
              .body(
                  new Result(
                      true,
                      HttpStatus.OK.value(),
                      "Get traveler requests successfully",
                      travelerRequests.stream()
                          .map(travelerRequestToTravelerRequestDtoConverter::convert)
                          .toList()));
        });
  }

  @PutMapping("/{travelerRequestId}")
  public ResponseEntity<Result> updateTravelerRequests(
      Authentication authentication,
      @RequestBody UpdateTravelerRequestDTO updateTravelerRequestDTO,
      @PathVariable("travelerRequestId") Long travelerRequestId) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          String email = ((CustomUserDetails) authentication.getPrincipal()).getEmail();

          TravelerRequest travelerRequest =
              travelerRequestService.updateTravelerRequests(
                  email, updateTravelerRequestDTO, travelerRequestId);

          if (travelerRequest == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(
                    new Result(
                        true,
                        HttpStatus.NOT_ACCEPTABLE.value(),
                        "You can not update this traveler request!"));
          }
          return ResponseEntity.status(HttpStatus.OK)
              .body(
                  new Result(
                      true,
                      HttpStatus.OK.value(),
                      "Update traveler request successfully",
                      travelerRequestToTravelerRequestDtoConverter.convert(travelerRequest)));
        });
  }

  @PatchMapping("/{travelerRequestId}")
  public ResponseEntity<Result> updateTravelRequestStatus(
      Authentication authentication,
      @RequestBody UpdateTravelerRequestDTO updateTravelerRequestDTO,
      @PathVariable("travelerRequestId") Long travelerRequestId) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          String email = ((CustomUserDetails) authentication.getPrincipal()).getEmail();

          TravelerRequest travelerRequest =
              travelerRequestService.updateTravelRequestStatus(
                  email, updateTravelerRequestDTO, travelerRequestId);

          if (travelerRequest == null) {
            return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(
                    new Result(
                        true,
                        HttpStatus.NOT_ACCEPTABLE.value(),
                        "You can not update this traveler request!"));
          }
          return ResponseEntity.status(HttpStatus.OK)
              .body(
                  new Result(
                      true,
                      HttpStatus.OK.value(),
                      "Update traveler request successfully",
                      travelerRequestToTravelerRequestDtoConverter.convert(travelerRequest)));
        });
  }
}
