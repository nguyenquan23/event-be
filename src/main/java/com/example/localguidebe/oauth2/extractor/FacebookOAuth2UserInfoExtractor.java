package com.example.localguidebe.oauth2.extractor;

import com.example.localguidebe.enums.RolesEnum;
import com.example.localguidebe.oauth2.OAuth2Provider;
import com.example.localguidebe.security.service.CustomUserDetails;
import java.util.Collections;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@Service
public class FacebookOAuth2UserInfoExtractor implements OAuth2UserInfoExtractor {
  @Override
  public CustomUserDetails extractUserInfo(OAuth2User oAuth2User) {
    CustomUserDetails customUserDetails = new CustomUserDetails();
    customUserDetails.setFullName(retrieveAttr("name", oAuth2User));
    customUserDetails.setEmail(retrieveAttr("email", oAuth2User));
    customUserDetails.setImageUrl(retrieveAttr("picture", oAuth2User));
    customUserDetails.setProvider(OAuth2Provider.FACEBOOK);
    customUserDetails.setAttributes(oAuth2User.getAttributes());
    customUserDetails.setAuthorities(
        Collections.singletonList(new SimpleGrantedAuthority(RolesEnum.TRAVELER.toString())));
    return customUserDetails;
  }

  @Override
  public boolean accepts(OAuth2UserRequest userRequest) {
    return OAuth2Provider.FACEBOOK
        .name()
        .equalsIgnoreCase(userRequest.getClientRegistration().getRegistrationId());
  }

  private String retrieveAttr(String attr, OAuth2User oAuth2User) {
    Object attribute = oAuth2User.getAttributes().get(attr);
    return attribute == null ? "" : attribute.toString();
  }
}
