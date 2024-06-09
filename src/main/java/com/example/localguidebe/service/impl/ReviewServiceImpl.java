package com.example.localguidebe.service.impl;

import com.example.localguidebe.converter.NotificationToNotificationDtoConverter;
import com.example.localguidebe.converter.ReviewToReviewResponseDto;
import com.example.localguidebe.converter.TourToTourDtoConverter;
import com.example.localguidebe.dto.requestdto.ReviewRequestDTO;
import com.example.localguidebe.dto.responsedto.ReviewResponseDTO;
import com.example.localguidebe.entity.Notification;
import com.example.localguidebe.entity.Review;
import com.example.localguidebe.entity.Tour;
import com.example.localguidebe.entity.User;
import com.example.localguidebe.enums.NotificationTypeEnum;
import com.example.localguidebe.repository.ReviewRepository;
import com.example.localguidebe.repository.TourRepository;
import com.example.localguidebe.repository.UserRepository;
import com.example.localguidebe.service.NotificationService;
import com.example.localguidebe.service.ReviewService;
import com.example.localguidebe.service.TourService;
import com.example.localguidebe.system.constants.NotificationMessage;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ReviewServiceImpl implements ReviewService {
  private final SimpMessagingTemplate messagingTemplate;
  private final NotificationToNotificationDtoConverter notificationToNotificationDtoConverter;
  private final ReviewRepository reviewRepository;
  private final UserRepository userRepository;
  private final TourRepository tourRepository;
  private final TourToTourDtoConverter tourToTourDtoConverter;
  private final TourService tourService;
  private final NotificationService notificationService;
  private final ReviewToReviewResponseDto reviewToReviewResponseDto;

  @Autowired
  public ReviewServiceImpl(
      SimpMessagingTemplate messagingTemplate,
      NotificationToNotificationDtoConverter notificationToNotificationDtoConverter,
      ReviewRepository reviewRepository,
      UserRepository userRepository,
      TourRepository tourRepository,
      TourToTourDtoConverter tourToTourDtoConverter,
      ReviewToReviewResponseDto reviewToReviewResponseDto,
      TourService tourService,
      NotificationService notificationService) {
    this.messagingTemplate = messagingTemplate;
    this.notificationToNotificationDtoConverter = notificationToNotificationDtoConverter;
    this.reviewRepository = reviewRepository;
    this.userRepository = userRepository;
    this.tourRepository = tourRepository;
    this.tourToTourDtoConverter = tourToTourDtoConverter;
    this.reviewToReviewResponseDto = reviewToReviewResponseDto;
    this.tourService = tourService;
    this.notificationService = notificationService;
  }

  @Override
  public Review addReviewForGuide(ReviewRequestDTO reviewRequestDTO, Long guideId, User traveler) {
    User guide = userRepository.findById(guideId).orElse(null);
    Review review =
        Review.builder()
            .comment(reviewRequestDTO.comment())
            .rating(reviewRequestDTO.rating())
            .guide(guide)
            .traveler(traveler)
            .createdAt(LocalDateTime.now())
            .isEdited(false)
            .build();
    Review newReview = reviewRepository.save(review);
    // notification send to guider
    Notification notification =
        notificationService.addNotification(
            guideId,
            traveler.getId(),
            guideId,
            NotificationTypeEnum.ADD_REVIEW_FOR_GUIDE,
            traveler.getFullName() + NotificationMessage.ADD_REVIEW_FOR_GUIDE);
    assert guide != null;
    messagingTemplate.convertAndSend(
        "/topic/" + guide.getEmail(), notificationToNotificationDtoConverter.convert(notification));
    return newReview;
  }

  @Override
  public List<ReviewResponseDTO> addReviewForTour(
      ReviewRequestDTO reviewRequestDTO, Long tourId, String email) {
    User traveler = userRepository.findUserByEmail(email);
    Tour tour = tourRepository.findById(tourId).orElseThrow();
    Review newReview =
        Review.builder()
            .tour(tourRepository.findById(tourId).orElseThrow())
            .comment(reviewRequestDTO.comment())
            .rating(reviewRequestDTO.rating())
            .createdAt(LocalDateTime.now())
            .traveler(traveler)
            .isEdited(false)
            .build();
    tour.getReviews().add(newReview);
    tourService.updateRatingForTour(tour);
    tourRepository.save(tour);

    // notification send to guider
    Notification notification =
        notificationService.addNotification(
            tour.getGuide().getId(),
            traveler.getId(),
            tourId,
            NotificationTypeEnum.ADD_REVIEW_FOR_TOUR,
            traveler.getFullName() + NotificationMessage.ADD_REVIEW_FOR_TOUR);

    messagingTemplate.convertAndSend(
        "/topic/" + tour.getGuide().getEmail(),
        notificationToNotificationDtoConverter.convert(notification));

    return tour.getReviews().stream()
        .map(reviewToReviewResponseDto::convert)
        .collect(Collectors.toList());
  }

  @Override
  public List<ReviewResponseDTO> getReviewForTour(Long tourId) {
    Tour tour = tourRepository.findById(tourId).orElseThrow();
    return tour.getReviews().stream()
        .map(reviewToReviewResponseDto::convert)
        .collect(Collectors.toList());
  }

  @Override
  public List<Review> getReviewsOfGuide(Long guideId, List<Integer> ratings, String sortBy) {
    if (ratings.isEmpty()) {
      ratings = Arrays.asList(1, 2, 3, 4, 5);
    }
    return reviewRepository.getReviewsByGuide(guideId, ratings, sortBy);
  }

  @Override
  public List<ReviewResponseDTO> editReviewForTour(Long reviewId, ReviewRequestDTO reviewRequestDTO)
      throws Exception {
    Review review = reviewRepository.findById(reviewId).orElseThrow();
    if (review.getIsEdited()) throw new Exception();
    review.setRating(reviewRequestDTO.rating());
    review.setComment(reviewRequestDTO.comment());
    review.setIsEdited(true);
    reviewRepository.save(review);
    tourService.updateRatingForTour(tourRepository.findTourByReviewsId(reviewId).orElseThrow());
    return reviewRepository.findAll().stream()
        .map(reviewToReviewResponseDto::convert)
        .collect(Collectors.toList());
  }

  @Override
  public boolean checkReviewByTraveler(Long reviewId, String email) {
    return reviewRepository.findAll().stream()
        .anyMatch(
            review -> review.getTraveler().getEmail().equals(email) && review.getId() == reviewId);
  }

  @Override
  public List<ReviewResponseDTO> deleteReviewForTour(Long reviewId) {
    Tour tour = tourRepository.findTourByReviewsId(reviewId).orElseThrow();
    reviewRepository.deleteById(reviewId);
    tourService.updateRatingForTour(tour);
    return reviewRepository.findAll().stream()
        .map(reviewToReviewResponseDto::convert)
        .collect(Collectors.toList());
  }

  @Override
  public boolean updateReviewForGuide(
      ReviewRequestDTO reviewRequestDTO, User traveler, Long reviewId) {
    if (traveler.getReviewsOfTraveler().stream()
        .noneMatch(review -> review.getId().equals(reviewId))) return false;
    Optional<Review> optionalReview = reviewRepository.findById(reviewId);
    if (optionalReview.isEmpty()) return false;
    Review review = optionalReview.get();
    if (review.getIsEdited()) return false;
    review.setIsEdited(true);
    review.setRating(reviewRequestDTO.rating());
    review.setComment(reviewRequestDTO.comment());
    reviewRepository.saveAndFlush(review);
    return true;
  }

  @Transactional
  @Override
  public boolean deleteReviewForGuide(User traveler, Long reviewId) {
    if (traveler.getReviewsOfTraveler().stream()
        .noneMatch(review -> review.getId().equals(reviewId))) return false;
    reviewRepository.deleteById(reviewId);
    return true;
  }
}
