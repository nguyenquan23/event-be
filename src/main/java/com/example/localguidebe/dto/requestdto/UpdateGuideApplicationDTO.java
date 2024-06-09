package com.example.localguidebe.dto.requestdto;

public record UpdateGuideApplicationDTO(
    Boolean isLicensedGuide,
    String transportation,
    Integer yearsOfExperience,
    String howGuideHearAboutUs) {}
