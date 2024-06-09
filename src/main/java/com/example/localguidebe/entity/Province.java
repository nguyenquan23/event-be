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
@Table(name = "province")
public class Province {
  @Id private String code;
  @Column private String name;
  @Column private String nameEn;
  @Column private String fullName;
  @Column private String fullNameEn;
  @Column private String codeName;

  @ManyToOne
  @JoinColumn(name = "administrative_unit_id")
  private AdministrativeUnit administrativeUnit;

  @ManyToOne
  @JoinColumn(name = "administrative_region_id")
  private AdministrativeRegion administrativeRegion;

  @OneToMany(mappedBy = "province")
  private List<District> districts = new ArrayList<>();
}
