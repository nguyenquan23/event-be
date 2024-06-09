package com.example.localguidebe.dto.responsedto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StatisticalTourDTO {
  private Long id;
  private String name;
  private Double pricePerTraveler;
  private Integer limitTraveler;
  private Double overallRating;
  private Long totalTravelerNumber;
  private Double totalRevenue;
  private Long totalBooking;
}
