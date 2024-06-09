package com.example.localguidebe.service;

import com.example.localguidebe.dto.requestdto.UpdatePersonalInformationDTO;
import com.example.localguidebe.dto.responsedto.SearchSuggestionResponseDTO;
import com.example.localguidebe.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface UserService {
  User findUserByEmail(String email);

  Optional<User> findById(Long id);

  User saveUser(User user);

  SearchSuggestionResponseDTO getGuidesAndGuideAddresses(String searchValue);

  List<User> getAllUser();

  Page<User> getGuides(
      Integer page,
      Integer limit,
      String sortBy,
      String order,
      Double ratingFilter,
      String searchValue);

  boolean isTravelerCanAddReviewForGuide(User traveler, Long guideId);

  User updatePersonalInformation(
      String email, UpdatePersonalInformationDTO updatePersonalInformationDTO);

  User deleteUser(User user);
}
