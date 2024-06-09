package com.example.localguidebe.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "district")
public class District {
  @Id private String code;
  @Column private String name;
  @Column private String nameEn;
  @Column private String fullName;
  @Column private String fullNameEn;
  @Column private String codeName;

  @ManyToOne
  @JoinColumn(name = "province_code")
  private Province province;

  @ManyToOne
  @JoinColumn(name = "administrative_unit_id")
  private AdministrativeUnit administrativeUnit;

  @OneToMany(mappedBy = "district")
  private List<Ward> wards = new ArrayList<>();
}
