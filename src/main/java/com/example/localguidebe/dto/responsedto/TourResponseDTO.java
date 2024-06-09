package com.example.localguidebe.dto.responsedto;

import com.example.localguidebe.dto.CategoryDTO;
import com.example.localguidebe.entity.*;
import lombok.*;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
public class TourResponseDTO {
    private Long id;
    private String name;
    private String description;
    private String transportation;
    private String includeService;
    private Integer duration;
    private String unit;
    private  String estimatedLocalCashNeeded;
    private Double pricePerTraveler;
    private Integer limitTraveler;
    private Double extraPrice;

    private String itinerary;
//    private User guide;
//    private List<TourStartTime> tourStartTimes = new ArrayList<>();
    private Set<CategoryDTO> categories = new HashSet<>();
//    private List<Review> reviews = new ArrayList<>();
//    private List<Booking> bookings = new ArrayList<>();
    private List<Image> images = new ArrayList<>();
//    private Set<Location> locations;
//    private Location location;
}
