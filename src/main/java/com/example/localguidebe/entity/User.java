package com.example.localguidebe.entity;

import com.example.localguidebe.oauth2.OAuth2Provider;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user")
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "id")
  private Long id;

  @Column(name = "full_name")
  private String fullName;

  @Column(name = "password")
  private String password;

  @Column(name = "email")
  private String email;

  @Column(name = "date_of_birht")
  private LocalDateTime dateOfBirth;

  @Column(name = "phone")
  private String phone;

  @Column(name = "address")
  private String address;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "guide")
  private List<BusySchedule> busySchedules = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "guide")
  private List<Tour> tours = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "guide")
  private List<Review> reviewsOfGuide = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "traveler")
  private List<Review> reviewsOfTraveler = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "traveler")
  private List<Invoice> invoices = new ArrayList<>();

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "user_role",
      joinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"),
      inverseJoinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"))
  private Set<Role> roles = new HashSet<>();

  @Column(columnDefinition = "TEXT")
  private String biography;

  @Column(columnDefinition = "TEXT")
  private String credential;

  @Column() private Double overallRating;

  @OneToMany(cascade = CascadeType.ALL, mappedBy = "guide")
  List<LanguageSkill> languageSkills = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "traveler")
  private List<TravelerRequest> travelerRequests = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "guide")
  private List<TravelerRequest> travelerRequestsOfGuide = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "sender")
  List<Notification> notificationSender = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "receiver")
  List<Notification> notificationReceiver = new ArrayList<>();

  @Column(columnDefinition = "boolean default false")
  private boolean isDeleted;

  @ManyToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "users")
  private List<Tour> wishListTour = new ArrayList<>();

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "user")
  List<GuideApplication> guideApplications = new ArrayList<>();

  @Column
  @Enumerated(EnumType.STRING)
  private OAuth2Provider oAuth2Provider;

  @Column(columnDefinition = "TEXT")
  private String rpToken;

  @Override
  public String toString() {
    return "{"
        + "id:"
        + id
        + ", fullName:"
        + fullName
        + ", password:"
        + password
        + ", email:"
        + email
        + ", dateOfBirth:"
        + dateOfBirth
        + ", phone:"
        + phone
        + ", address:"
        + address
        + "}";
  }
}
