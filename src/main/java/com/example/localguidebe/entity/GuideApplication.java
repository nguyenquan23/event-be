package com.example.localguidebe.entity;

import com.example.localguidebe.enums.GuideApplicationStatus;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "guide_application")
public class GuideApplication {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column() private Boolean isLicensedGuide;
  @Column() private String transportation;
  @Column() private Integer yearsOfExperience;
  @Column() private String howGuideHearAboutUs;

  @Column()
  @Enumerated(EnumType.STRING)
  private GuideApplicationStatus status;

  @Column() private String reasonDeny;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;
}
