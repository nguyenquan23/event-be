package com.example.localguidebe.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "administrative_region")
public class AdministrativeRegion {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column private String name;
  @Column private String name_en;
  @Column private String code_name;
  @Column private String code_name_en;

  @OneToMany(mappedBy = "administrativeRegion")
  private List<Province> provinces = new ArrayList<>();
}
