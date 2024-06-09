package com.example.localguidebe.oauth2;

import com.example.localguidebe.entity.Role;
import com.example.localguidebe.entity.User;
import com.example.localguidebe.enums.RolesEnum;
import com.example.localguidebe.oauth2.extractor.OAuth2UserInfoExtractor;
import com.example.localguidebe.security.service.CustomUserDetails;
import com.example.localguidebe.service.RoleService;
import com.example.localguidebe.service.UserService;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Component;

@Component
public class CustomOAuth2UserService extends DefaultOAuth2UserService {
  Logger logger = LoggerFactory.getLogger(CustomOAuth2UserService.class);

  private final RoleService roleService;
  private final UserService userService;
  private final List<OAuth2UserInfoExtractor> oAuth2UserInfoExtractors;

  public CustomOAuth2UserService(
      RoleService roleService,
      UserService userService,
      List<OAuth2UserInfoExtractor> oAuth2UserInfoExtractors) {
    this.roleService = roleService;
    this.userService = userService;
    this.oAuth2UserInfoExtractors = oAuth2UserInfoExtractors;
  }

  @Override
  public OAuth2User loadUser(OAuth2UserRequest userRequest) {
    OAuth2User oAuth2User = super.loadUser(userRequest);
    Optional<OAuth2UserInfoExtractor> oAuth2UserInfoExtractorOptional =
        oAuth2UserInfoExtractors.stream()
            .filter(oAuth2UserInfoExtractor -> oAuth2UserInfoExtractor.accepts(userRequest))
            .findFirst();
    if (oAuth2UserInfoExtractorOptional.isEmpty()) {
      throw new InternalAuthenticationServiceException("The OAuth2 provider is not supported yet");
    }

    CustomUserDetails customUserDetails =
        oAuth2UserInfoExtractorOptional.get().extractUserInfo(oAuth2User);
    User user = upsertUser(customUserDetails);
    customUserDetails.setId(user.getId());
    return customUserDetails;
  }

  private User upsertUser(CustomUserDetails customUserDetails) {
    User user = userService.findUserByEmail(customUserDetails.getEmail());
    if (user == null) {
      user = new User();
      user.setFullName(customUserDetails.getFullName());
      user.setEmail(customUserDetails.getEmail());
      user.setOAuth2Provider(customUserDetails.getProvider());
      Role role = roleService.findByName(RolesEnum.TRAVELER);
      Set<Role> roles = new HashSet<>();
      roles.add(role);
    } else {
      user.setEmail(customUserDetails.getEmail());
    }
    // TODO: add new image here
    return userService.saveUser(user);
  }
}
