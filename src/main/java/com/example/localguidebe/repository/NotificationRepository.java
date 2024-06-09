package com.example.localguidebe.repository;

import com.example.localguidebe.entity.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotificationRepository extends JpaRepository<Notification,Long> {
    Page<Notification> getNotificationsByReceiverEmail(String email, Pageable pageable);
}
