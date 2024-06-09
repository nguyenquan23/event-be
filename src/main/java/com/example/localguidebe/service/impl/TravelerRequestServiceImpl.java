package com.example.localguidebe.service.impl;

import com.example.localguidebe.converter.NotificationToNotificationDtoConverter;
import com.example.localguidebe.dto.requestdto.AddTravelerRequestDTO;
import com.example.localguidebe.dto.requestdto.UpdateTravelerRequestDTO;
import com.example.localguidebe.entity.Notification;
import com.example.localguidebe.entity.Tour;
import com.example.localguidebe.entity.TravelerRequest;
import com.example.localguidebe.entity.User;
import com.example.localguidebe.enums.NotificationTypeEnum;
import com.example.localguidebe.enums.RolesEnum;
import com.example.localguidebe.enums.TravelerRequestStatus;
import com.example.localguidebe.repository.TourRepository;
import com.example.localguidebe.repository.TravelerRequestRepository;
import com.example.localguidebe.service.NotificationService;
import com.example.localguidebe.service.TravelerRequestService;
import com.example.localguidebe.service.UserService;
import com.example.localguidebe.system.constants.NotificationMessage;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class TravelerRequestServiceImpl implements TravelerRequestService {
  private final TravelerRequestRepository travelerRequestRepository;
  private final UserService userService;
  private final TourRepository tourRepository;
  private final NotificationService notificationService;
  private final SimpMessagingTemplate messagingTemplate;
  private final NotificationToNotificationDtoConverter notificationToNotificationDtoConverter;

  public TravelerRequestServiceImpl(
      TravelerRequestRepository travelerRequestRepository,
      UserService userService,
      TourRepository tourRepository,
      NotificationService notificationService,
      SimpMessagingTemplate messagingTemplate,
      NotificationToNotificationDtoConverter notificationToNotificationDtoConverter) {
    this.travelerRequestRepository = travelerRequestRepository;
    this.userService = userService;
    this.tourRepository = tourRepository;
    this.notificationService = notificationService;
    this.messagingTemplate = messagingTemplate;
    this.notificationToNotificationDtoConverter = notificationToNotificationDtoConverter;
  }

  @Override
  public TravelerRequest findTravelerRequestById(Long id) {
    return travelerRequestRepository.findById(id).orElse(null);
  }

  @Override
  public TravelerRequest addTravelerRequest(
      String travelerEmail, AddTravelerRequestDTO addTravelerRequestDTO) {
    User traveler = userService.findUserByEmail(travelerEmail);
    User guide = userService.findById(addTravelerRequestDTO.guideId()).orElse(null);
    if (guide == null) return null;

    // if traveler A adds 1st request for guide B and that is PENDING, can not add 2nd request to
    // that guide
    if (addTravelerRequestDTO.travelerRequestStatus().equals(TravelerRequestStatus.PENDING))
      if (guide.getTravelerRequestsOfGuide().stream()
          .anyMatch(
              travelerRequest ->
                  travelerRequest.getTraveler().getEmail().equals(travelerEmail)
                      && travelerRequest.getStatus().equals(TravelerRequestStatus.PENDING)))
        return null;
    if (addTravelerRequestDTO.travelerRequestId() != null) {
      travelerRequestRepository.deleteById(addTravelerRequestDTO.travelerRequestId());
    }
    TravelerRequest travelerRequest =
        TravelerRequest.builder()
            .destination(addTravelerRequestDTO.destination())
            .maxPricePerPerson(addTravelerRequestDTO.maxPricePerPerson())
            .numberOfTravelers(addTravelerRequestDTO.numberOfTravelers())
            .message(addTravelerRequestDTO.message())
            .transportation(String.join(", ", addTravelerRequestDTO.transportation()))
            .unit(addTravelerRequestDTO.unit())
            .duration(addTravelerRequestDTO.duration())
            .status(addTravelerRequestDTO.travelerRequestStatus())
            .traveler(traveler)
            .guide(guide)
            .build();

    // notification send to guide
    Notification guideNotification =
        notificationService.addNotification(
            guide.getId(),
            traveler.getId(),
            travelerRequest.getId(),
            NotificationTypeEnum.ADD_NEW_TRAVELER_REQUEST,
            NotificationMessage.ADD_NEW_TRAVELER_REQUEST);

    messagingTemplate.convertAndSend(
        "/topic/" + guide.getEmail(),
        notificationToNotificationDtoConverter.convert(guideNotification));

    return travelerRequestRepository.save(travelerRequest);
  }

  @Override
  public List<TravelerRequest> getTravelerRequests(String email) {
    return travelerRequestRepository.getTravelerRequests(email);
  }

  @Override
  public TravelerRequest updateTravelerRequests(
      String email, UpdateTravelerRequestDTO updateTravelerRequestDTO, Long travelerRequestId) {
    TravelerRequest travelerRequest = findTravelerRequestById(travelerRequestId);
    if (travelerRequest == null) return null;
    if (!travelerRequest.getTraveler().getEmail().equals(email)
        && !travelerRequest.getGuide().getEmail().equals(email)) return null;
    BeanUtils.copyProperties(updateTravelerRequestDTO, travelerRequest, "transportation", "status");
    travelerRequest.setTransportation(String.join(", ", updateTravelerRequestDTO.transportation()));
    return travelerRequestRepository.save(travelerRequest);
  }

  @Override
  public TravelerRequest updateTravelRequestStatus(
      String email, UpdateTravelerRequestDTO updateTravelerRequestDTO, Long travelerRequestId) {
    TravelerRequest travelerRequest = findTravelerRequestById(travelerRequestId);
    if (travelerRequest == null) return null;

    // If user is traveler, they only cancel or make draft there request
    User user = userService.findUserByEmail(email);
    if (user.getRoles().stream().noneMatch(role -> role.getName().equals(RolesEnum.GUIDER))
        && !updateTravelerRequestDTO.status().equals(TravelerRequestStatus.CANCELED)
        && !updateTravelerRequestDTO.status().equals(TravelerRequestStatus.DRAFT)) return null;

    if (!travelerRequest.getTraveler().getEmail().equals(email)
        && !travelerRequest.getGuide().getEmail().equals(email)) return null;

    travelerRequest.setStatus(updateTravelerRequestDTO.status());
    return travelerRequestRepository.save(travelerRequest);
  }

  @Override
  public void updateStatusAndTourForTravelerRequest(Long requestId, Long tourId) {
    TravelerRequest travelerRequest = travelerRequestRepository.findById(requestId).orElse(null);
    Tour travelerRequestTour = tourRepository.findById(tourId).orElseThrow();
    if (travelerRequest != null) {
      travelerRequestTour.setIsForSpecificTraveler(true);
      travelerRequest.setStatus(TravelerRequestStatus.DONE);
      travelerRequest.setTour(travelerRequestTour);
    }
    travelerRequestRepository.save(travelerRequest);
  }
}
