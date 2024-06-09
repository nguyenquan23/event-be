package com.example.localguidebe.dto;

import java.time.LocalDateTime;

public record ReviewDTO(
    Long id, String comment, Integer rating, LocalDateTime createAt, UserDTO traveler) {}
