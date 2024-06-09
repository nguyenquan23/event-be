package com.example.localguidebe.dto.requestdto;

public record ResetPasswordRequestDTO(
    String email, // for send email reset pasword
    String token,
    String password) {}
