package com.example.localguidebe.dto.requestdto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequestDTO {
    private String email;
    private String password;
}
