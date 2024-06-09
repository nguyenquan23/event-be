package com.example.localguidebe.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "guide_detail")
public class GuideDetail {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;


  @Column(columnDefinition = "TEXT")
  private String biography;


  @Column(columnDefinition = "TEXT")
  private String credential;

  @Column() private Double overallRating;
}
