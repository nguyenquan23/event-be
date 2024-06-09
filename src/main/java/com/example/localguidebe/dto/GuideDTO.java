package com.example.localguidebe.dto;

import java.time.LocalDateTime;
import java.util.List;

public record GuideDTO(
    Long id,
    String email,
    String fullName,
    LocalDateTime dateOfBirth,
    String phone,
    String address,
    String biography,
    String credential,
    Double overallRating,
    List<LanguageSkillDTO> languageSkill,
    List<ImageDTO> images) {}
