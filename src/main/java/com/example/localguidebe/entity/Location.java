package com.example.localguidebe.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "location")
public class Location {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "name")
  private String name;

  @Column(name = "latitude")
  private Double latitude;

  @Column(name = "longitude")
  private Double longitude;

  @Column(name = "address")
  private String address;

  @ManyToMany(mappedBy = "locations", fetch = FetchType.LAZY)
  private List<Tour> tours = new ArrayList<>();

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }

    if (!(o instanceof Location that)) {
      return false;
    }

    return getId().equals(that.getId());
  }

  public Location(String name, Double latitude, Double longitude) {
    this.name = name;
    this.latitude = latitude;
    this.longitude = longitude;
  }
}
