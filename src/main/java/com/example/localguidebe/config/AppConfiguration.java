package com.example.localguidebe.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import java.util.List;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class AppConfiguration {
  @Value("${spring.profiles.active}")
  private String activeProfile;

  @Bean
  public Cloudinary getCloudinary() {
    return new Cloudinary(
        ObjectUtils.asMap(
            "cloud_name", "dh06dk7vs",
            "api_key", "861591919956872",
            "api_secret", "iB44CBRCuvXp49vjdA6YjjbZ0rM",
            "secure", true));
  }

  @Bean
  public CorsFilter corsFilter() {
    CorsConfiguration corsConfiguration = new CorsConfiguration();
    if (activeProfile.equals("dev")) {
      corsConfiguration.addAllowedOriginPattern("*");
      System.out.println("dev");
    }

    if (activeProfile.equals("prod")) {
      corsConfiguration.addAllowedOriginPattern("*");
      corsConfiguration.setAllowedOrigins(List.of("https://interns-local-guide.vercel.app/"));
      System.out.println("prod");
    }
    corsConfiguration.addAllowedHeader("*");
    corsConfiguration.addAllowedMethod("*");
    corsConfiguration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", corsConfiguration);
    return new CorsFilter(source);
  }

  @Bean
  public RestTemplate restTemplate() {
    return new RestTemplate();
  }

  @Bean
  public JavaMailSender getJavaMailSender() {
    JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
    mailSender.setHost("smtp.gmail.com");
    mailSender.setPort(587);

    mailSender.setUsername("localguide15124@gmail.com");
    mailSender.setPassword("qick bfsw cnqb ikoy");

    Properties props = mailSender.getJavaMailProperties();
    props.put("mail.transport.protocol", "smtp");
    props.put("mail.smtp.auth", "true");
    props.put("mail.smtp.starttls.enable", "true");

    return mailSender;
  }
}
