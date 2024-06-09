package com.example.localguidebe.converter;

import com.example.localguidebe.dto.UserDTO;
import com.example.localguidebe.entity.Role;
import com.example.localguidebe.entity.User;
import java.util.Comparator;
import org.springframework.stereotype.Component;

@Component
public class UserToUserDtoConverter {
  private final RoleToRoleDtoConverter roleToRoleDtoConverter;

  public UserToUserDtoConverter(RoleToRoleDtoConverter roleToRoleDtoConverter) {
    this.roleToRoleDtoConverter = roleToRoleDtoConverter;
  }

  public UserDTO convert(User source) {
    Role highestRole =
        source.getRoles().stream()
            .max(Comparator.comparingInt(role -> role.getName().getValue()))
            .orElse(null);
    return new UserDTO(
        source.getId(),
        source.getEmail(),
        source.getFullName(),
        source.getDateOfBirth(),
        source.getPhone(),
        source.getAddress(),
        highestRole == null ? null : roleToRoleDtoConverter.convert(highestRole));
  }
}
