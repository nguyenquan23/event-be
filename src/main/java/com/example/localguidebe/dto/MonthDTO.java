package com.example.localguidebe.dto;

import lombok.*;

@Getter
@Builder
public class MonthDTO {
  private Double revenue;
  private Integer bookingOfNumber;
  private Integer month;
}
