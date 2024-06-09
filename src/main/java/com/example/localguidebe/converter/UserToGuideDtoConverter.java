package com.example.localguidebe.converter;

import com.example.localguidebe.dto.GuideDTO;
import com.example.localguidebe.entity.User;
import com.example.localguidebe.enums.AssociateName;
import com.example.localguidebe.service.ImageService;
import org.springframework.stereotype.Component;

@Component
public class UserToGuideDtoConverter {
  private final LanguageSkillToLanguageSkillDtoConverter languageSkillToLanguageSkillDtoConverter;
  private final ImageService imageService;
  private final ImageToImageDtoConverter imageToImageDtoConverter;

  public UserToGuideDtoConverter(
      LanguageSkillToLanguageSkillDtoConverter languageSkillToLanguageSkillDtoConverter,
      ImageService imageService,
      ImageToImageDtoConverter imageToImageDtoConverter) {
    this.languageSkillToLanguageSkillDtoConverter = languageSkillToLanguageSkillDtoConverter;
    this.imageService = imageService;
    this.imageToImageDtoConverter = imageToImageDtoConverter;
  }

  public GuideDTO convert(User source) {
    return new GuideDTO(
        source.getId(),
        source.getEmail(),
        source.getFullName(),
        source.getDateOfBirth(),
        source.getPhone(),
        source.getAddress(),
        source.getBiography(),
        source.getCredential(),
        source.getOverallRating() == null ? null : source.getOverallRating(),
        source.getLanguageSkills().stream()
            .map(languageSkillToLanguageSkillDtoConverter::convert)
            .toList(),
        imageService
            .getImageByAssociateIddAndAssociateName(source.getId(), AssociateName.USER)
            .stream()
            .map(imageToImageDtoConverter::convert)
            .toList());
  }
}
