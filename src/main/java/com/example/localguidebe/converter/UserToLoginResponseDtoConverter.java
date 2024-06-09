package com.example.localguidebe.converter;

import com.example.localguidebe.dto.responsedto.LoginResponseDTO;
import com.example.localguidebe.entity.User;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UserToLoginResponseDtoConverter {
    private final UserToUserDtoConverter userToUserDtoConverter;

    public UserToLoginResponseDtoConverter(UserToUserDtoConverter userToUserDtoConverter) {
        this.userToUserDtoConverter = userToUserDtoConverter;
    }

    public LoginResponseDTO convert(User source, String accessToken) {
        return new LoginResponseDTO(
                accessToken,
                userToUserDtoConverter.convert(source)
        );
    }
}
