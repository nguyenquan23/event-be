package com.example.localguidebe.dto.requestdto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserAuthDTO {
    private Integer id;
    private String email;
    private String password;
    private String token;

    @Override
    public String toString() {
        return "{" +
                "id:" + id +
                ", password:" + password +
                ", email:" + email +
                ", token:" + token  +
                "}";
    }

}
