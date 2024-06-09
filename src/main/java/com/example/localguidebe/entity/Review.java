package com.example.localguidebe.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "review")
public class Review {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "comment", columnDefinition = "TEXT")
  private String comment;

  @Column(name = "rating")
  private Integer rating;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "parent_id")
  private Review parentReview;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "parentReview")
  private List<Review> childReview = new ArrayList<>();

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "tour_id")
  private Tour tour;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "guide_id")
  private User guide;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "traveler_id")
  private User traveler;

  @Column(columnDefinition = "boolean default false")
  private Boolean isEdited;

  @PostPersist
  @PostUpdate
  @PostRemove
  public void updateGuideOverallRating() {
    if (this.guide != null) {
      Double newOverallRating =
          BigDecimal.valueOf(
                  guide.getReviewsOfGuide().stream()
                      .mapToDouble(Review::getRating)
                      .average()
                      .orElse(0))
              .setScale(1, RoundingMode.HALF_UP)
              .doubleValue();
      guide.setOverallRating(newOverallRating);
    }
  }
}
