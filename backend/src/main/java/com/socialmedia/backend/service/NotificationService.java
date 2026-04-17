package com.socialmedia.backend.service;

import com.socialmedia.backend.entity.Notification;
import com.socialmedia.backend.entity.User;
import com.socialmedia.backend.repository.NotificationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public void createLikeNotification(User receiver) {
        Notification n = new Notification();
        n.setUser(receiver);
        n.setType("LIKE");
        n.setIsRead(false);
        n.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(n);
    }

     // ===== COMMENT NOTIFICATION =====
    public void createCommentNotification(User receiver) {
        createNotification(receiver, "COMMENT");
    }

    // ===== COMMON =====
    private void createNotification(User receiver, String type) {
        Notification n = new Notification();
        n.setUser(receiver);
        n.setType(type);
        n.setIsRead(false);
        n.setCreatedAt(LocalDateTime.now());
        notificationRepository.save(n);
    }
}
