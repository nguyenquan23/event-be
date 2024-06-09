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
@Table(name = "administrative_unit")
public class AdministrativeUnit {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column private String fullName;
  @Column private String fullNameEn;
  @Column private String shortName;
  @Column private String shortNameEn;
  @Column private String codeName;
  @Column private String codeNameEn;

  @OneToMany(mappedBy = "administrativeUnit")
  private List<Province> provinces = new ArrayList<>();

  @OneToMany(mappedBy = "administrativeUnit")
  private List<District> districts = new ArrayList<>();

  @OneToMany(mappedBy = "administrativeUnit")
  private List<Ward> wards = new ArrayList<>();
}
