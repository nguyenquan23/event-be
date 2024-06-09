package com.example.localguidebe.entity;

import com.example.localguidebe.enums.TravelerRequestStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "traveler_request")
public class TravelerRequest {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "transportation")
  private String transportation;

  @Column(name = "duration")
  private Integer duration;

  @Column(name = "unit")
  private String unit;

  @Column(name = "max_price_per_person")
  private Double maxPricePerPerson;

  @Column(name = "number_of_travelers")
  private Integer numberOfTravelers;

  @Column(name = "destination")
  private String destination;

  @Enumerated(EnumType.STRING)
  @Column(name = "status")
  private TravelerRequestStatus status;

  @Column(name = "message")
  private String message;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "traveler_id")
  private User traveler;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "guide_id")
  private User guide;

  @OneToOne
  @JoinColumn(name = "tour_id")
  private Tour tour;
}
