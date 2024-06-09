package com.example.localguidebe.converter;

import com.example.localguidebe.dto.responsedto.ReviewResponseDTO;
import com.example.localguidebe.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewToReviewResponseDto {
  public final UserToUserDtoConverter userToUserDtoConverter;

  public ReviewToReviewResponseDto(UserToUserDtoConverter userToUserDtoConverter) {
    this.userToUserDtoConverter = userToUserDtoConverter;
  }

  public ReviewResponseDTO convert(Review review) {
    return new ReviewResponseDTO(
        review.getId(),
        review.getComment(),
        review.getRating(),
        review.getCreatedAt(),
        userToUserDtoConverter.convert(review.getTraveler()),
        review.getChildReview());
  }
}
