package com.example.localguidebe.entity;

import com.example.localguidebe.enums.BookingStatusEnum;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import lombok.*;
import org.hibernate.validator.constraints.Range;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "booking")
public class Booking {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "start_date")
  private LocalDateTime startDate;

  @Range(min = 0, max = 50)
  @Column(name = "number_travelers")
  private Integer numberTravelers;

  @Range(min = 0, max = 100000000)
  @Column(name = "price")
  private Double price;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private BookingStatusEnum status;

  @Column(columnDefinition = "boolean default false")
  private boolean isDeleted;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "cart_id")
  private Cart cart;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "tour_id")
  private Tour tour;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "invoice_id")
  private Invoice invoice;

  @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
  @JoinColumn(name = "tour_dupe_id")
  private TourDupe tourDupe;

}
