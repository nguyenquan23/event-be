package com.example.localguidebe.entity;

import com.example.localguidebe.enums.InvoiceStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "invoice")
public class Invoice {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "price_total")
  private Double priceTotal;

  @Column(name = "vnd_price")
  private Double vndPrice;

  @Column(name = "conversion_rate")
  private Double conversionRate;

  @Column(name = "create_at")
  private LocalDateTime createAt;

  @Column() private String fullName;

  @Column() private String phone;

  @Column() private String email;

  @Column(name = "vnp_TxnRef")
  private String vnpTxnRef;

  @Column(name = "status")
  @Enumerated(EnumType.STRING)
  private InvoiceStatus status;

  @Column() private Double refundVndPrice;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "traveler_id")
  private User traveler;

  @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL, mappedBy = "invoice")
  private List<Booking> bookings = new ArrayList<>();
}
