package com.example.localguidebe.dto.responsedto;

import com.example.localguidebe.dto.UserDTO;

public record LoginResponseDTO (
     String accessToken,
     UserDTO user
){}
