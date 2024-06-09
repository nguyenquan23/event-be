package com.example.localguidebe.converter;

import com.example.localguidebe.dto.responsedto.ResultInSearchSuggestionDTO;
import com.example.localguidebe.entity.Image;
import com.example.localguidebe.entity.Tour;
import com.example.localguidebe.entity.User;
import com.example.localguidebe.enums.AssociateName;
import com.example.localguidebe.service.ImageService;
import java.util.List;
import org.springframework.stereotype.Component;

@Component
public class ToResultInSearchSuggestionDtoConverter {
  private final ImageService imageService;

  public ToResultInSearchSuggestionDtoConverter(ImageService imageService) {
    this.imageService = imageService;
  }

  public ResultInSearchSuggestionDTO convert(Object object) {
    List<Image> images;
    if (object instanceof Tour tour) {
      images =
          imageService.getImageByAssociateIddAndAssociateName(tour.getId(), AssociateName.TOUR);
      return new ResultInSearchSuggestionDTO(
          tour.getId(), tour.getName(), images.isEmpty() ? null : images.get(0).getImageLink());
    } else if (object instanceof User user) {
      images =
          imageService.getImageByAssociateIddAndAssociateName(user.getId(), AssociateName.USER);
      return new ResultInSearchSuggestionDTO(
          user.getId(), user.getFullName(), images.isEmpty() ? null : images.get(0).getImageLink());
    }
    return new ResultInSearchSuggestionDTO(null, null, null);
  }
}
