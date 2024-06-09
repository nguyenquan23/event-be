package com.example.localguidebe.service;

import com.example.localguidebe.dto.NotificationDTO;
import com.example.localguidebe.entity.Notification;
import com.example.localguidebe.enums.NotificationTypeEnum;
import java.util.List;

public interface NotificationService {
  List<NotificationDTO> getNotifications(Integer page, Integer limit, String email);

  Notification addNotification(
      Long receiverId,
      Long senderId,
      Long associateId,
      NotificationTypeEnum notificationTypeEnum,
      String message);

  boolean updateIsReadNotification(Long notificationId);
}
