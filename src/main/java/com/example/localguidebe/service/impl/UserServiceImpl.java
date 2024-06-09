package com.example.localguidebe.service.impl;

import com.example.localguidebe.converter.ToResultInSearchSuggestionDtoConverter;
import com.example.localguidebe.dto.requestdto.UpdatePersonalInformationDTO;
import com.example.localguidebe.dto.responsedto.ResultInSearchSuggestionDTO;
import com.example.localguidebe.dto.responsedto.SearchSuggestionResponseDTO;
import com.example.localguidebe.entity.Role;
import com.example.localguidebe.entity.User;
import com.example.localguidebe.enums.RolesEnum;
import com.example.localguidebe.repository.UserRepository;
import com.example.localguidebe.service.UserService;
import com.example.localguidebe.utils.AddressUtils;
import com.example.localguidebe.utils.JsonUtils;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {
  Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

  private UserRepository userRepository;

  private final ToResultInSearchSuggestionDtoConverter toResultInSearchSuggestionDtoConverter;

  public UserServiceImpl(
      ToResultInSearchSuggestionDtoConverter toResultInSearchSuggestionDtoConverter) {
    this.toResultInSearchSuggestionDtoConverter = toResultInSearchSuggestionDtoConverter;
  }

  @Autowired
  public void setUserRepository(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @Override
  public User findUserByEmail(String email) {
    return userRepository.findUserByEmail(email);
  }

  @Override
  public Optional<User> findById(Long id) {
    return userRepository.findById(id);
  }

  @Override
  public User saveUser(User user) {
    return this.userRepository.saveAndFlush(user);
  }

  @Override
  public List<User> getAllUser() {
    return this.userRepository.findAll();
  }

  @Override
  public Page<User> getGuides(
      Integer page,
      Integer limit,
      String sortBy,
      String order,
      Double ratingFilter,
      String searchValue) {
    Sort sort = order.equals("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
    Pageable paging = PageRequest.of(page, limit, sort);
    Specification<Object> specification =
        Specification.where(
                (root, query, criteriaBuilder) -> {
                  Join<User, Role> userRoleJoin = root.join("roles", JoinType.INNER);
                  return criteriaBuilder.equal(userRoleJoin.get("name"), RolesEnum.GUIDER);
                })
            .and(
                (root, query, criteriaBuilder) ->
                    criteriaBuilder.or(
                        criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("fullName")),
                            "%" + searchValue.toLowerCase() + "%"),
                        criteriaBuilder.like(
                            criteriaBuilder.lower(root.get("address")),
                            "%" + searchValue.toLowerCase() + "%")));

    if (ratingFilter != null) {
      specification =
          specification.and(
              (root, query, criteriaBuilder) ->
                  criteriaBuilder.greaterThanOrEqualTo(root.get("overallRating"), ratingFilter));
    }
    return userRepository.findAll(specification, paging);
  }

  @Transactional
  @Override
  public boolean isTravelerCanAddReviewForGuide(User traveler, Long guideId) {

    List<Long> guideIdsInBooking =
        traveler.getInvoices().stream()
            .flatMap(invoice -> invoice.getBookings().stream())
            .filter(booking -> booking.getTour().getGuide().getId().equals(guideId))
            .map(booking -> booking.getTour().getGuide().getId())
            .toList();
    if (guideIdsInBooking.isEmpty()) return false;

    List<Long> guideIdsInReview =
        traveler.getReviewsOfTraveler().stream()
            .filter(
                review -> {
                  if (review.getGuide() != null && review.getGuide().getId() != null) {
                    return review.getGuide().getId().equals(guideId);
                  }
                  return false;
                })
            .map(review -> review.getGuide().getId())
            .toList();
    return guideIdsInBooking.size() > guideIdsInReview.size();
  }

  @Override
  public User updatePersonalInformation(
      String email, UpdatePersonalInformationDTO updatePersonalInformationDTO) {
    User user = findUserByEmail(email);
    if (user == null) return null;
    BeanUtils.copyProperties(
        updatePersonalInformationDTO,
        user,
        JsonUtils.getNullPropertyNames(updatePersonalInformationDTO));
    return userRepository.save(user);
  }

  @Override
  public User deleteUser(User user) {
    user.setDeleted(true);
    return userRepository.save(user);
  }

  @Override
  public SearchSuggestionResponseDTO getGuidesAndGuideAddresses(String searchValue) {
    List<User> guiders = userRepository.findByRoles_Name(RolesEnum.GUIDER);
    List<String> addressesFiltered =
        guiders.stream()
            .map(User::getAddress)
            .distinct()
            .filter(
                address -> {
                  if (address == null) return false;
                  return AddressUtils.removeVietnameseAccents(address)
                      .toLowerCase()
                      .contains(AddressUtils.removeVietnameseAccents(searchValue).toLowerCase());
                })
            .toList();
    List<ResultInSearchSuggestionDTO> guidersFiltered =
        guiders.stream()
            .sorted(
                Comparator.comparing(
                    User::getOverallRating, Comparator.nullsLast(Comparator.reverseOrder())))
            .filter(
                guider -> {
                  if (guider.getFullName() == null) return false;
                  return AddressUtils.removeVietnameseAccents(guider.getFullName())
                      .toLowerCase()
                      .contains(AddressUtils.removeVietnameseAccents(searchValue).toLowerCase());
                })
            .map(toResultInSearchSuggestionDtoConverter::convert)
            .toList();
    return new SearchSuggestionResponseDTO(addressesFiltered, guidersFiltered);
  }
}
