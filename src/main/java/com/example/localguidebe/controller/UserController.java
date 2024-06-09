package com.example.localguidebe.controller;

import com.example.localguidebe.converter.UserToUserDtoConverter;
import com.example.localguidebe.dto.requestdto.ChangePasswordDTO;
import com.example.localguidebe.dto.requestdto.UpdatePersonalInformationDTO;
import com.example.localguidebe.dto.responsedto.IsCanReviewResponseDTO;
import com.example.localguidebe.entity.User;
import com.example.localguidebe.enums.RolesEnum;
import com.example.localguidebe.security.service.CustomUserDetails;
import com.example.localguidebe.service.TourService;
import com.example.localguidebe.service.UserService;
import com.example.localguidebe.system.Result;
import com.example.localguidebe.utils.AuthUtils;
import java.util.Objects;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
  private final UserService userService;
  private final UserToUserDtoConverter userToUserDtoConverter;
  private final TourService tourService;

  public UserController(
      UserService userService,
      UserToUserDtoConverter userToUserDtoConverter,
      TourService tourService) {
    this.userService = userService;
    this.userToUserDtoConverter = userToUserDtoConverter;
    this.tourService = tourService;
  }

  BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();

  @PutMapping("")
  public ResponseEntity<Result> updatePersonalInformation(
      Authentication authentication,
      @RequestBody UpdatePersonalInformationDTO updatePersonalInformationDTO) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          try {
            String email = ((CustomUserDetails) authentication.getPrincipal()).getEmail();
            User updatedUser =
                userService.updatePersonalInformation(email, updatePersonalInformationDTO);
            if (updatedUser == null) {
              return ResponseEntity.status(HttpStatus.NOT_FOUND)
                  .body(new Result(true, HttpStatus.NOT_FOUND.value(), "This user is not exist"));
            }
            return ResponseEntity.status(HttpStatus.OK)
                .body(
                    new Result(
                        true,
                        HttpStatus.OK.value(),
                        "Update personal information successfully",
                        userToUserDtoConverter.convert(updatedUser)));
          } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                    new Result(
                        true,
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Can not update personal information"));
          }
        });
  }

  @GetMapping("/is-can-review-guide/{guideId}")
  public ResponseEntity<Result> isCanCommentOnGuide(
      Authentication authentication, @PathVariable("guideId") Long guideId) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          String travelerEmail = ((CustomUserDetails) authentication.getPrincipal()).getEmail();
          User traveler = userService.findUserByEmail(travelerEmail);
          if (!userService.isTravelerCanAddReviewForGuide(traveler, guideId)) {
            return ResponseEntity.status(HttpStatus.OK)
                .body(
                    new Result(
                        false,
                        HttpStatus.OK.value(),
                        "You can't add review for this guide",
                        new IsCanReviewResponseDTO(false)));
          } else
            return ResponseEntity.status(HttpStatus.OK)
                .body(
                    new Result(
                        true,
                        HttpStatus.OK.value(),
                        "You can add review for this guide",
                        new IsCanReviewResponseDTO(true)));
        });
  }

  @GetMapping("/is-can-review-tour/{tourId}")
  public ResponseEntity<Result> isCanCommentOnTour(
      Authentication authentication, @PathVariable("tourId") Long tourId) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          String travelerEmail = ((CustomUserDetails) authentication.getPrincipal()).getEmail();
          Long travelerId = ((CustomUserDetails) authentication.getPrincipal()).getId();
          if (!tourService.checkBookingByTraveler(tourId, travelerEmail)
              || !tourService.checkExistingReviewsByTraveler(travelerId, tourId)) {
            return ResponseEntity.status(HttpStatus.OK)
                .body(
                    new Result(
                        false,
                        HttpStatus.OK.value(),
                        "You can't add review for this tour",
                        new IsCanReviewResponseDTO(false)));
          } else
            return ResponseEntity.status(HttpStatus.OK)
                .body(
                    new Result(
                        true,
                        HttpStatus.OK.value(),
                        "You can add review for this tour",
                        new IsCanReviewResponseDTO(true)));
        });
  }

  @GetMapping("/me")
  public ResponseEntity<Result> getSelfInformation(Authentication authentication) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          try {
            String email = ((CustomUserDetails) authentication.getPrincipal()).getEmail();
            User user = userService.findUserByEmail(email);
            if (user == null) {
              return ResponseEntity.status(HttpStatus.OK)
                  .body(new Result(false, HttpStatus.OK.value(), "The user not exist"));
            }
            return ResponseEntity.status(HttpStatus.OK)
                .body(
                    new Result(
                        false,
                        HttpStatus.OK.value(),
                        "Get user information successfully",
                        userToUserDtoConverter.convert(user)));

          } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                    new Result(
                        true,
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Can not get personal information"));
          }
        });
  }

  @DeleteMapping("/{userId}")
  public ResponseEntity<Result> deleteUser(
      Authentication authentication, @PathVariable("userId") Long userId) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          try {
            String email = ((CustomUserDetails) authentication.getPrincipal()).getEmail();
            User userInSession = userService.findUserByEmail(email);
            User userBeDeleted = userService.findById(userId).orElse(null);

            if (userBeDeleted == null) {
              return ResponseEntity.status(HttpStatus.OK)
                  .body(new Result(false, HttpStatus.OK.value(), "The user not exist"));
            }
            if (!Objects.equals(userInSession.getId(), userBeDeleted.getId())) {
              if (userToUserDtoConverter.convert(userInSession).role() != RolesEnum.ADMIN) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(
                        new Result(
                            false, HttpStatus.CONFLICT.value(), "You can not delete this user"));
              }
            }
            return ResponseEntity.status(HttpStatus.OK)
                .body(
                    new Result(
                        true,
                        HttpStatus.OK.value(),
                        "Delete user successfully",
                        userToUserDtoConverter.convert(userService.deleteUser(userBeDeleted))));

          } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(
                    new Result(
                        false,
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "You can not delete this user"));
          }
        });
  }

  @PatchMapping("")
  public ResponseEntity<Result> changPassword(
      Authentication authentication, @RequestBody ChangePasswordDTO changePasswordDTO) {
    return AuthUtils.checkAuthentication(
        authentication,
        () -> {
          String email = ((CustomUserDetails) authentication.getPrincipal()).getEmail();
          User user = userService.findUserByEmail(email);
          if (bCryptPasswordEncoder.matches(changePasswordDTO.password(), user.getPassword())) {
            if (!bCryptPasswordEncoder.matches(
                changePasswordDTO.newPassword(), user.getPassword())) {
              user.setPassword(bCryptPasswordEncoder.encode(changePasswordDTO.newPassword()));
              userService.saveUser(user);
              return ResponseEntity.status(HttpStatus.OK)
                  .body(new Result(false, HttpStatus.OK.value(), "Change password successfully"));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(
                    new Result(
                        false,
                        HttpStatus.CONFLICT.value(),
                        "New password and old password could not be same"));
          }
          return ResponseEntity.status(HttpStatus.CONFLICT)
              .body(new Result(false, HttpStatus.CONFLICT.value(), "Your old password not match"));
        });
  }
}
