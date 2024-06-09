package com.example.localguidebe.entity;

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
@Table(name = "cart")
public class Cart {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @OneToOne
  @JoinColumn(name = "traveler_id", nullable = false)
  private User traveler;

  @OneToMany(fetch = FetchType.LAZY,cascade = CascadeType.ALL, mappedBy = "cart")
  private List<Booking> bookings = new ArrayList<>();
}
