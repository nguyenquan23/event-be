package com.example.localguidebe.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenProvider {
  private static final Key SECRET_KEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);
  private static final long EXPIRATION_TIME_MS = 86400000;

  private static final long EXPIRATION_TIME_MS_FOR_RESET_PASSWORD = 600000;

  public String generateToken(String subject) {
    Date now = new Date();
    Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME_MS);

    return Jwts.builder()
        .setSubject(subject)
        .setIssuedAt(now)
        .setExpiration(expirationDate)
        .signWith(SECRET_KEY)
        .compact();
  }

  public String generateTokenResetPassword(String subject) {
    Date now = new Date();
    Date expirationDate = new Date(now.getTime() + EXPIRATION_TIME_MS_FOR_RESET_PASSWORD);

    return Jwts.builder()
        .setSubject(subject)
        .setIssuedAt(now)
        .setExpiration(expirationDate)
        .signWith(SECRET_KEY)
        .compact();
  }

  public boolean validateToken(String token) {
    try {
      Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token);
      return true;
    } catch (Exception e) {
      return false;
    }
  }

  public String getEmailFromToken(String token) {
    Claims claims =
        Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
    return claims.getSubject();
  }
}
