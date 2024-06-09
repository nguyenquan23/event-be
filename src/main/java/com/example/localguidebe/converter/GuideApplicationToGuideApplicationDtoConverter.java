package com.example.localguidebe.converter;

import com.example.localguidebe.dto.GuideApplicationDTO;
import com.example.localguidebe.entity.GuideApplication;
import com.example.localguidebe.enums.AssociateName;
import com.example.localguidebe.service.ImageService;
import org.springframework.stereotype.Component;

@Component
public class GuideApplicationToGuideApplicationDtoConverter {
  private final UserToUserDtoConverter userToUserDtoConverter;
  private final ImageService imageService;

  public GuideApplicationToGuideApplicationDtoConverter(
      UserToUserDtoConverter userToUserDtoConverter, ImageService imageService) {
    this.userToUserDtoConverter = userToUserDtoConverter;
    this.imageService = imageService;
  }

  public GuideApplicationDTO convert(GuideApplication source) {
    return new GuideApplicationDTO(
        source.getId(),
        source.getIsLicensedGuide(),
        source.getTransportation(),
        source.getYearsOfExperience(),
        source.getHowGuideHearAboutUs(),
        source.getUser().getBiography(),
        source.getStatus(),
        source.getReasonDeny(),
        imageService.getImageByAssociateIddAndAssociateName(
            source.getId(), AssociateName.GUIDE_APPLICATION),
        userToUserDtoConverter.convert(source.getUser()));
  }
}
