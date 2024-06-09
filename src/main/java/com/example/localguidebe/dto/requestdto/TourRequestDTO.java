package com.example.localguidebe.dto.requestdto;

import com.example.localguidebe.dto.LocationDTO;
import com.example.localguidebe.dto.TourStartTimeDTO;
import com.example.localguidebe.entity.*;

import java.sql.Time;
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
public class TourRequestDTO {
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
  private String address;
  private String itinerary;
  private User guide;
  private List<Time> startTimes = new ArrayList<>();
  private Set<Category> categories = new HashSet<>();
  private List<Review> reviews = new ArrayList<>();
  private List<Booking> bookings = new ArrayList<>();
  private List<String> images = new ArrayList<>();
  private List<LocationDTO> locations ;
  private Location location;
}
