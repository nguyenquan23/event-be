package com.example.localguidebe.controller;

import com.example.localguidebe.converter.GuideApplicationToGuideApplicationDtoConverter;
import com.example.localguidebe.dto.requestdto.AddGuideApplicationDTO;
import com.example.localguidebe.dto.requestdto.UpdateGuideApplicationDTO;
import com.example.localguidebe.dto.requestdto.UpdateGuideApplicationStatus;
import com.example.localguidebe.entity.GuideApplication;
import com.example.localguidebe.service.GuideApplicationService;
import com.example.localguidebe.system.Result;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/guide-applications")
public class GuideApplicationController {
  private final GuideApplicationService guideApplicationService;
  private final GuideApplicationToGuideApplicationDtoConverter
      guideApplicationToGuideApplicationDtoConverter;

  public GuideApplicationController(
      GuideApplicationService guideApplicationService,
      GuideApplicationToGuideApplicationDtoConverter
          guideApplicationToGuideApplicationDtoConverter) {
    this.guideApplicationService = guideApplicationService;
    this.guideApplicationToGuideApplicationDtoConverter =
        guideApplicationToGuideApplicationDtoConverter;
  }

  @PostMapping("")
  public ResponseEntity<Result> addGuideApplication(
      @RequestBody AddGuideApplicationDTO addGuideApplicationDTO) {
    try {
      GuideApplication guideApplication =
          guideApplicationService.addGuideApplication(addGuideApplicationDTO);
      if (guideApplication == null) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(
                new Result(
                    false,
                    HttpStatus.CONFLICT.value(),
                    "User has existed or your account does not exist"));
      }
      return ResponseEntity.status(HttpStatus.OK)
          .body(
              new Result(
                  true,
                  HttpStatus.OK.value(),
                  "Add guide application successfully",
                  guideApplicationToGuideApplicationDtoConverter.convert(guideApplication)));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              new Result(
                  true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Add guide application failed"));
    }
  }

  @GetMapping("")
  public ResponseEntity<Result> getGuideApplications() {
    try {
      List<GuideApplication> guideApplications = guideApplicationService.findAll();
      return ResponseEntity.status(HttpStatus.OK)
          .body(
              new Result(
                  true,
                  HttpStatus.OK.value(),
                  "Get guide applications successfully",
                  guideApplications.stream()
                      .map(guideApplicationToGuideApplicationDtoConverter::convert)
                      .toList()));

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              new Result(
                  true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Get guide applications failed"));
    }
  }

  @GetMapping("/{id}")
  public ResponseEntity<Result> getGuideApplication(@PathVariable("id") Long id) {
    try {
      GuideApplication guideApplication = guideApplicationService.findById(id);
      if (guideApplication == null) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(
                new Result(false, HttpStatus.CONFLICT.value(), "Guide application does not exist"));
      }
      return ResponseEntity.status(HttpStatus.OK)
          .body(
              new Result(
                  true,
                  HttpStatus.OK.value(),
                  "Get guide application successfully",
                  guideApplicationToGuideApplicationDtoConverter.convert(guideApplication)));

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              new Result(
                  true, HttpStatus.INTERNAL_SERVER_ERROR.value(), "Get guide application failed"));
    }
  }

  @PatchMapping("/{id}")
  // TODO: add authorization
  public ResponseEntity<Result> updateGuideApplicationStatus(
      Authentication authentication,
      @PathVariable("id") Long id,
      @RequestBody() UpdateGuideApplicationStatus updateGuideApplicationStatus) {
    try {
      boolean isUpdated =
          guideApplicationService.updateGuideApplicationStatus(id, updateGuideApplicationStatus);
      if (!isUpdated) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(
                new Result(false, HttpStatus.CONFLICT.value(), "Guide application does not exist"));
      }
      return ResponseEntity.status(HttpStatus.OK)
          .body(new Result(true, HttpStatus.OK.value(), "Add guide application successfully"));

    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              new Result(
                  true,
                  HttpStatus.INTERNAL_SERVER_ERROR.value(),
                  "Update guide application failed " + e.getLocalizedMessage()));
    }
  }

  @PutMapping("/{id}")
  public ResponseEntity<Result> updateGuideApplication(
      Authentication authentication,
      @PathVariable("id") Long id,
      @RequestBody() UpdateGuideApplicationDTO updateGuideApplicationDTO) {
    try {
      boolean isUpdated =
          guideApplicationService.updateGuideApplication(id, updateGuideApplicationDTO);
      if (!isUpdated) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
            .body(
                new Result(
                    false,
                    HttpStatus.CONFLICT.value(),
                    "Guide application does not exist or your applicant is accepted"));
      }
      return ResponseEntity.status(HttpStatus.OK)
          .body(new Result(true, HttpStatus.OK.value(), "Update guide application successfully"));

    } catch (RuntimeException e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              new Result(
                  true,
                  HttpStatus.INTERNAL_SERVER_ERROR.value(),
                  "Update guide application failed"));
    }
  }
}
