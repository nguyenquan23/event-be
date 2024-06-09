package com.example.localguidebe.oauth2;

import com.example.localguidebe.security.jwt.JwtTokenProvider;
import com.example.localguidebe.security.service.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  @Value("${oauth2.redirectUri}")
  private String redirectUri;

  @Autowired public JwtTokenProvider jwtTokenProvider;

  @Override
  public void onAuthenticationSuccess(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {
    handle(request, response, authentication);
    super.clearAuthenticationAttributes(request);
  }

  @Override
  protected void handle(
      HttpServletRequest request, HttpServletResponse response, Authentication authentication)
      throws IOException {
    String targetUrl =
        redirectUri.isEmpty() ? determineTargetUrl(request, response, authentication) : redirectUri;
    CustomUserDetails customUserDetails = (CustomUserDetails) authentication.getPrincipal();
    String token = jwtTokenProvider.generateToken(customUserDetails.getEmail());
    targetUrl =
        UriComponentsBuilder.fromUriString(targetUrl)
            .queryParam("token", token)
            .build()
            .toUriString();
    getRedirectStrategy().sendRedirect(request, response, targetUrl);
  }
}
