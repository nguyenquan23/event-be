package com.example.localguidebe.entity;

import com.example.localguidebe.dto.TourDTO;
import com.example.localguidebe.enums.TourStatusEnum;
import com.example.localguidebe.utils.JsonUtils;
import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "tour_dupe")
public class TourDupe {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name", nullable = false)
  private String name;

  @Column(name = "description", columnDefinition = "TEXT")
  private String description;

  @Column(name = "transportation", columnDefinition = "TEXT")
  private String transportation;

  @Column(name = "include_service", columnDefinition = "TEXT")
  private String includeService;

  @Column(name = "duration")
  private Integer duration;

  @Column(name = "unit")
  private String unit;

  @Column(name = "estimated_local_cash_needed", columnDefinition = "TEXT")
  private String estimatedLocalCashNeeded;

  @Column(name = "price_per_traveler")
  private Double pricePerTraveler;

  @Column(name = "limit_traveler")
  private Integer limitTraveler;

  @Column(name = "extra_price")
  private Double extraPrice;

  @Column(name = "overall_rating", columnDefinition = "double default 0.0")
  private Double overallRating = 0.0;

  @Column(name = "itinerary", columnDefinition = "TEXT")
  private String itinerary;

  @Column(name = "is_deleted", columnDefinition = "boolean default false")
  private Boolean isDeleted = Boolean.FALSE;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private TourStatusEnum status;

  @Column(columnDefinition = "TEXT")
  private String guide;

  @Column(columnDefinition = "TEXT")
  private String tourStartTimes;

  @Column(columnDefinition = "TEXT")
  private String categories;

  @Column(columnDefinition = "TEXT")
  private String reviews;

  @Column(columnDefinition = "TEXT")
  private String locations;

  @Column(columnDefinition = "TEXT")
  private String images;

  @Column(name = "is_for_specific_traveler", columnDefinition = "boolean default false")
  private Boolean isForSpecificTraveler = Boolean.FALSE;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "tourDupe")
  private List<Booking> bookings = new ArrayList<>();

  public TourDupe(TourDTO tour) throws Exception {
    this.name = tour.getName();
    this.description = tour.getDescription();
    this.transportation = tour.getTransportation();
    this.includeService = tour.getIncludeService();
    this.duration = tour.getDuration();
    this.unit = tour.getUnit();
    this.estimatedLocalCashNeeded = tour.getEstimatedLocalCashNeeded();
    this.pricePerTraveler = tour.getPricePerTraveler();
    this.limitTraveler = tour.getLimitTraveler();
    this.overallRating = tour.getOverallRating();
    this.itinerary = tour.getItinerary();
    this.guide = JsonUtils.convertObjectToJson(tour.getGuide());
    this.tourStartTimes = JsonUtils.convertObjectToJson(tour.getStartTimes());
    this.categories = JsonUtils.convertObjectToJson(tour.getCategories());
    this.reviews = JsonUtils.convertObjectToJson(tour.getReviewDTOS());
    this.locations = JsonUtils.convertObjectToJson(tour.getLocations());
    this.images = JsonUtils.convertObjectToJson(tour.getImages());
  }

  @Override
  public String toString() {
    return "TourDupe{"
        + "id="
        + id
        + ", name='"
        + name
        + '\''
        + ", description='"
        + description
        + '\''
        + ", transportation='"
        + transportation
        + '\''
        + ", includeService='"
        + includeService
        + '\''
        + ", duration="
        + duration
        + ", unit='"
        + unit
        + '\''
        + ", estimatedLocalCashNeeded='"
        + estimatedLocalCashNeeded
        + '\''
        + ", pricePerTraveler="
        + pricePerTraveler
        + ", limitTraveler="
        + limitTraveler
        + ", extraPrice="
        + extraPrice
        + ", overallRating="
        + overallRating
        + ", itinerary='"
        + itinerary
        + '\''
        + ", isDeleted="
        + isDeleted
        + ", guide='"
        + guide
        + '\''
        + ", tourStartTimes='"
        + tourStartTimes
        + '\''
        + ", categories='"
        + categories
        + '\''
        + ", reviews='"
        + reviews
        + '\''
        + ", locations='"
        + locations
        + '\''
        + ", images='"
        + images
        + '\''
        + ", isForSpecificTraveler="
        + isForSpecificTraveler
        + '}';
  }
}
