package com.example.localguidebe.dto.requestdto;

import java.time.LocalDateTime;
import java.util.List;

public record AddGuideApplicationDTO(
    Long userId,
    String email,
    String password,
    String fullName,
    LocalDateTime dateOfBirth,
    String phone,
    String address,
    Boolean isLicensedGuide,
    String transportation,
    Integer yearsOfExperience,
    String howGuideHearAboutUs,
    String biography,
    List<String> licenseImages) {}
