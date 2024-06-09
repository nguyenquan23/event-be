package com.example.localguidebe.service.impl;

import com.example.localguidebe.converter.NotificationToNotificationDtoConverter;
import com.example.localguidebe.dto.NotificationDTO;
import com.example.localguidebe.entity.Notification;
import com.example.localguidebe.entity.User;
import com.example.localguidebe.enums.NotificationTypeEnum;
import com.example.localguidebe.repository.NotificationRepository;
import com.example.localguidebe.service.NotificationService;
import com.example.localguidebe.service.UserService;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class NotificationServiceImpl implements NotificationService {

  private final NotificationRepository notificationRepository;
  private final NotificationToNotificationDtoConverter notificationToNotificationDtoConverter;
  private final UserService userService;

  public NotificationServiceImpl(
      NotificationToNotificationDtoConverter notificationToNotificationDtoConverter,
      UserService userService,
      NotificationRepository notificationRepository) {
    this.notificationRepository = notificationRepository;
    this.notificationToNotificationDtoConverter = notificationToNotificationDtoConverter;
    this.userService = userService;
  }

  @Override
  public List<NotificationDTO> getNotifications(Integer page, Integer limit, String email) {
    Sort sort = Sort.by("notificationDate").descending();
    Pageable paging = PageRequest.of(page, limit, sort);
    return notificationRepository.getNotificationsByReceiverEmail(email, paging).stream()
        .map(notificationToNotificationDtoConverter::convert)
        .toList();
  }

  @Override
  public Notification addNotification(
      Long receiverId,
      Long senderId,
      Long associateId,
      NotificationTypeEnum notificationTypeEnum,
      String message) {
    Notification newNotification = Notification.builder().build();
    if (receiverId != null) {
      User receiver = userService.findById(receiverId).orElse(null);
      newNotification.setReceiver(receiver);
    }
    if (senderId != null) {
      User sender = userService.findById(senderId).orElse(null);
      newNotification.setSender(sender);
    }

    newNotification.setNotificationDate(LocalDateTime.now());
    newNotification.setAssociateId(associateId);
    newNotification.setNotificationType(notificationTypeEnum);
    newNotification.setMessage(message);
    return notificationRepository.save(newNotification);
  }

  @Override
  public boolean updateIsReadNotification(Long notificationId) {
    Notification notification = notificationRepository.findById(notificationId).orElse(null);
    if (notification == null) return false;
    notification.setRead(true);
    notificationRepository.save(notification);
    return true;
  }
}
