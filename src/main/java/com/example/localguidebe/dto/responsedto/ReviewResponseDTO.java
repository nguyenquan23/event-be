package com.example.localguidebe.dto.responsedto;

import com.example.localguidebe.dto.UserDTO;
import com.example.localguidebe.entity.Review;
import java.time.LocalDateTime;
import java.util.List;

public record ReviewResponseDTO(
    Long id,
    String comment,
    Integer rating,
    LocalDateTime createAt,
    UserDTO traveler,
    List<Review> childReview) {}
