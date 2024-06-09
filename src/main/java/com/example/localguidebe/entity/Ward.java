package com.example.localguidebe.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "ward")
public class Ward {
  @Id private String code;
  @Column private String name;
  @Column private String nameEn;
  @Column private String fullName;
  @Column private String fullNameEn;
  @Column private String codeName;

  @ManyToOne
  @JoinColumn(name = "district_code")
  private District district;

  @ManyToOne
  @JoinColumn(name = "administrative_unit_id")
  private AdministrativeUnit administrativeUnit;
}
