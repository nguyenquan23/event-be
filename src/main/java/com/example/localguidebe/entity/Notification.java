package com.example.localguidebe.entity;

import com.example.localguidebe.enums.NotificationTypeEnum;
import jakarta.persistence.*;
import java.time.LocalDateTime;

import lombok.*;

@Builder
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "notification")
public class Notification {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column private String message;
  @Column private LocalDateTime notificationDate;
  @Column private boolean isRead;

  @Column
  @Enumerated(EnumType.STRING)
  private NotificationTypeEnum notificationType;

  @Column private Long associateId;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "sender_id")
  User sender;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "receiver_id")
  User receiver;
}
