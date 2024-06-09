package com.example.localguidebe.security.service;

import com.example.localguidebe.oauth2.OAuth2Provider;
import java.util.Collection;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

@Getter
@Setter
public class CustomUserDetails implements UserDetails, OAuth2User {
  private Long id;
  private String password;
  private String email;
  private String imageUrl;
  private String fullName;
  private OAuth2Provider provider;
  private Map<String, Object> attributes;
  private Collection<? extends GrantedAuthority> authorities;

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return fullName;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getName() {
    return fullName;
  }
}
