package com.example.localguidebe.dto;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class StatisticalBookingDTO {
  private Object cityProvince;
  private Double totalPrice;
  private Long totalBookings;
}
