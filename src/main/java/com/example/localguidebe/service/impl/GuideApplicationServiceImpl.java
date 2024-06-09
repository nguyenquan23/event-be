package com.example.localguidebe.service.impl;

import com.example.localguidebe.dto.requestdto.AddGuideApplicationDTO;
import com.example.localguidebe.dto.requestdto.UpdateGuideApplicationDTO;
import com.example.localguidebe.dto.requestdto.UpdateGuideApplicationStatus;
import com.example.localguidebe.entity.GuideApplication;
import com.example.localguidebe.entity.Role;
import com.example.localguidebe.entity.User;
import com.example.localguidebe.enums.AssociateName;
import com.example.localguidebe.enums.FolderName;
import com.example.localguidebe.enums.GuideApplicationStatus;
import com.example.localguidebe.enums.RolesEnum;
import com.example.localguidebe.repository.GuideApplicationRepository;
import com.example.localguidebe.service.GuideApplicationService;
import com.example.localguidebe.service.ImageService;
import com.example.localguidebe.service.RoleService;
import com.example.localguidebe.service.UserService;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GuideApplicationServiceImpl implements GuideApplicationService {
  private final UserService userService;
  private final RoleService roleService;
  private final GuideApplicationRepository guideApplicationRepository;
  private final ImageService imageService;

  public GuideApplicationServiceImpl(
      UserService userService,
      RoleService roleService,
      GuideApplicationRepository guideApplicationRepository,
      ImageService imageService) {
    this.userService = userService;
    this.roleService = roleService;
    this.guideApplicationRepository = guideApplicationRepository;
    this.imageService = imageService;
  }

  BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Override
  public List<GuideApplication> findAll() {
    return guideApplicationRepository.findAll();
  }

  @Override
  public GuideApplication findById(Long id) {
    return guideApplicationRepository.findById(id).orElse(null);
  }

  @Transactional
  @Override
  public GuideApplication addGuideApplication(AddGuideApplicationDTO addGuideApplicationDTO) {
    User user;
    if (addGuideApplicationDTO.userId() != null) {
      user = userService.findById(addGuideApplicationDTO.userId()).orElse(null);
      if (user == null) return null;
      user.setPhone(addGuideApplicationDTO.phone());
      user.setDateOfBirth(addGuideApplicationDTO.dateOfBirth());
      user.setFullName(addGuideApplicationDTO.fullName());
      user.setAddress(addGuideApplicationDTO.address());
      user.setBiography(addGuideApplicationDTO.biography());
      user = userService.saveUser(user);
    } else {
      if (userService.findUserByEmail(addGuideApplicationDTO.email()) != null) return null;
      Set<Role> role = new HashSet<>();
      role.add(roleService.findByName(RolesEnum.TRAVELER));
      User newUser =
          User.builder()
              .email(addGuideApplicationDTO.email())
              .address(addGuideApplicationDTO.address())
              .fullName(addGuideApplicationDTO.fullName())
              .password(passwordEncoder.encode(addGuideApplicationDTO.password()))
              .phone(addGuideApplicationDTO.phone())
              .dateOfBirth(addGuideApplicationDTO.dateOfBirth())
              .biography(addGuideApplicationDTO.biography())
              .roles(role)
              .build();
      user = userService.saveUser(newUser);
    }
    GuideApplication newGuideApplication =
        guideApplicationRepository.save(
            GuideApplication.builder()
                .isLicensedGuide(addGuideApplicationDTO.isLicensedGuide())
                .transportation(addGuideApplicationDTO.transportation())
                .yearsOfExperience(addGuideApplicationDTO.yearsOfExperience())
                .howGuideHearAboutUs(addGuideApplicationDTO.howGuideHearAboutUs())
                .status(GuideApplicationStatus.PENDING)
                .user(user)
                .build());

    if (addGuideApplicationDTO.isLicensedGuide()) {
      addGuideApplicationDTO
          .licenseImages()
          .forEach(
              imageInBase64 ->
                  imageService.addImage(
                      imageInBase64,
                      newGuideApplication.getId(),
                      AssociateName.GUIDE_APPLICATION,
                      FolderName.guide_application));
    }
    return newGuideApplication;
  }

  @Transactional
  @Override
  public boolean updateGuideApplicationStatus(
      Long id, UpdateGuideApplicationStatus updateGuideApplicationStatus) {
    GuideApplication guideApplication = guideApplicationRepository.findById(id).orElse(null);
    if (guideApplication == null) return false;

    guideApplication.setStatus(updateGuideApplicationStatus.status());
    if (updateGuideApplicationStatus.status().equals(GuideApplicationStatus.ACCEPTED))
      guideApplication.getUser().getRoles().add(roleService.findByName(RolesEnum.GUIDER));
    if (updateGuideApplicationStatus.status().equals(GuideApplicationStatus.DENIED)) {
      guideApplication.setReasonDeny(updateGuideApplicationStatus.reasonDeny());
    }
    guideApplicationRepository.save(guideApplication);
    return true;
  }

  @Transactional
  @Override
  public boolean updateGuideApplication(
      Long id, UpdateGuideApplicationDTO updateGuideApplicationDTO) {
    GuideApplication guideApplication = guideApplicationRepository.findById(id).orElse(null);
    if (guideApplication == null) return false;
    if (guideApplication.getStatus().equals(GuideApplicationStatus.ACCEPTED)) return false;
    BeanUtils.copyProperties(updateGuideApplicationDTO, guideApplication);
    guideApplicationRepository.save(guideApplication);
    return true;
  }
}
