package com.example.localguidebe.utils;

import com.example.localguidebe.security.jwt.JwtTokenProvider;
import com.example.localguidebe.system.Result;
import java.util.function.Supplier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

public class AuthUtils {

  public String generateAccessToken(
      String email,
      String password,
      AuthenticationManager authenticationManager,
      JwtTokenProvider jwtTokenProvider) {

    try {
      Authentication authentication =
          authenticationManager.authenticate(
              new UsernamePasswordAuthenticationToken(email, password));
      String token = jwtTokenProvider.generateToken(email);
      return token;
    } catch (Exception e) {
      return "token was not created successfully";
    }
  }

  public static ResponseEntity<Result> checkAuthentication(
      Authentication authentication, Supplier<ResponseEntity<Result>> responseSupplier) {
    if (authentication == null || !authentication.isAuthenticated()) {
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
          .body(new Result(false, HttpStatus.UNAUTHORIZED.value(), "Let's login"));
    }
    return responseSupplier.get(); // Execute the supplied logic if authentication is successful
  }
}
