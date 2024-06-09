package com.example.localguidebe.converter;

import com.example.localguidebe.dto.ReviewDTO;
import com.example.localguidebe.entity.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewToReviewDtoConverter {
  private final UserToUserDtoConverter userToUserDtoConverter;

  public ReviewToReviewDtoConverter(UserToUserDtoConverter userToUserDtoConverter) {
    this.userToUserDtoConverter = userToUserDtoConverter;
  }

  public ReviewDTO convert(Review source) {
    return new ReviewDTO(
        source.getId(),
        source.getComment(),
        source.getRating(),
        source.getCreatedAt(),
        userToUserDtoConverter.convert(source.getTraveler()));
  }
}
