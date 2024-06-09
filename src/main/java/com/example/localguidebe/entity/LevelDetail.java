package com.example.localguidebe.entity;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "level_detail")
public class LevelDetail {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

 @Column private String level;


  @Column(columnDefinition = "TEXT")
  private String description;

  @OneToMany(mappedBy = "levelDetail",fetch = FetchType.LAZY)
  List<LanguageSkill> languageSkills = new ArrayList<>();
}
