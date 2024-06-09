package com.example.localguidebe.dto;

import com.example.localguidebe.enums.NotificationTypeEnum;
import java.time.LocalDateTime;

public record NotificationDTO(
    Long id,
    String message,
    LocalDateTime notificationDate,
    boolean isRead,
    NotificationTypeEnum notificationType,
    Long associateId,
    UserDTO sender,
    UserDTO receiver) {}
