package com.example.localguidebe.enums;

import lombok.Getter;

@Getter
public enum RolesEnum {
  TRAVELER(1),
  GUIDER(2),
  ADMIN(3);

  private final int value;

  RolesEnum(int value) {
    this.value = value;
  }
}
