package com.example.localguidebe.dto.responsedto;

import java.time.LocalDateTime;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class StatisticalGuideDTO {
  private Long id;
  private String fullName;
  private String email;
  private String phone;
  private String address;
  private LocalDateTime dateOfBirth;
  private Double overallRating;
  private Long totalTravelerNumber;
  private Double totalRevenue;
  private Long totalBooking;
}
