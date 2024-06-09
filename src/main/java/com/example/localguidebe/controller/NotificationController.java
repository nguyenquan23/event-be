package com.example.localguidebe.controller;

import com.example.localguidebe.dto.NotificationDTO;
import com.example.localguidebe.security.service.CustomUserDetails;
import com.example.localguidebe.service.NotificationService;
import com.example.localguidebe.system.Result;
import com.example.localguidebe.utils.AuthUtils;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notifications")
public class NotificationController {
  private final NotificationService notificationService;

  public NotificationController(NotificationService notificationService) {
    this.notificationService = notificationService;
  }

  @GetMapping("")
  public ResponseEntity<Result> getNotification(
      Authentication authentication,
      @RequestParam(required = false, defaultValue = "0") Integer page,
      @RequestParam(required = false, defaultValue = "5") Integer limit) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          try {
            String email = ((CustomUserDetails) authentication.getPrincipal()).getEmail();
            List<NotificationDTO> notificationDTOs =
                notificationService.getNotifications(page, limit, email);
            if (notificationDTOs.isEmpty()) {
              return ResponseEntity.status(HttpStatus.OK)
                  .body(new Result(true, HttpStatus.OK.value(), "Notifications not found"));
            }
            return ResponseEntity.status(HttpStatus.OK)
                .body(
                    new Result(
                        true,
                        HttpStatus.OK.value(),
                        "Get notifications successfully",
                        notificationDTOs));
          } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                    new Result(
                        false,
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Get notifications successfully"));
          }
        });
  }

  @PatchMapping("{id}")
  public ResponseEntity<Result> updateIsReadNotification(@PathVariable("id") Long notificationId) {
    try {
      boolean isUpdated = notificationService.updateIsReadNotification(notificationId);
      if (!isUpdated) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .body(new Result(true, HttpStatus.NOT_FOUND.value(), "The notification not exist"));
      }
      return ResponseEntity.status(HttpStatus.OK)
          .body(
              new Result(
                  true, HttpStatus.OK.value(), "The notification has been updated successfully"));
    } catch (Exception e) {
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
          .body(
              new Result(
                  true,
                  HttpStatus.INTERNAL_SERVER_ERROR.value(),
                  "The notification has been updated unsuccessfully."));
    }
  }
}
