package com.example.localguidebe.dto;

import com.example.localguidebe.entity.Image;
import com.example.localguidebe.enums.GuideApplicationStatus;
import java.util.List;

public record GuideApplicationDTO(
    Long id,
    Boolean isLicensedGuide,
    String transportation,
    Integer yearsOfExperience,
    String howGuideHearAboutUs,
    String biography,
    GuideApplicationStatus status,
    String reasonDeny,
    List<Image> images,
    UserDTO user) {}
