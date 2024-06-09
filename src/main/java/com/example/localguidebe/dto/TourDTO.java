package com.example.localguidebe.dto;

import com.example.localguidebe.entity.*;
import com.example.localguidebe.enums.TourStatusEnum;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TourDTO {
  private Long id;

  private String name;

  private String description;

  private String transportation;

  private String includeService;

  private Integer duration;

  private String unit;

  private String estimatedLocalCashNeeded;

  private Double pricePerTraveler;

  private Integer limitTraveler;

  private Double overallRating;

  private String itinerary;

  private boolean isDeleted;

  private String address;

  private UserDTO guide;

  private TourStatusEnum status;

  private List<String> startTimes = new ArrayList<>();

  private Set<CategoryDTO> categories = new HashSet<>();

  private List<ReviewDTO> reviewDTOS = new ArrayList<>();

  private List<Image> images = new ArrayList<>();

  private List<LocationDTO> locations = new ArrayList<>();

  private Boolean isForSpecificTraveler;
}
