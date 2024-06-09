package com.example.localguidebe.entity;

import com.example.localguidebe.enums.TypeBusyDayEnum;
import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "busy_schedule")
public class BusySchedule {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "busy_date")
  private LocalDateTime busyDate;

  @Column
  @Enumerated(EnumType.STRING)
  private TypeBusyDayEnum typeBusyDay;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "guide_id")
  private User guide;

  @Override
  public boolean equals(Object object) {
    if (this == object) return true;
    if (!(object instanceof BusySchedule that)) return false;
    return this.getId().equals(that.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }
}
