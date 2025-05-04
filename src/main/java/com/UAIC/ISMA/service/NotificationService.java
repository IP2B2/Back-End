package com.UAIC.ISMA.service;

import com.UAIC.ISMA.dao.Notification;
import com.UAIC.ISMA.repository.NotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;

    @Autowired
    public NotificationService(NotificationRepository notificationRepository) {
        this.notificationRepository = notificationRepository;
    }

    public List<Notification> getAllNotifications() {
        try {
            return notificationRepository.findAll();
        } catch (Exception ex) {
            throw new RuntimeException("Error fetching notifications", ex);
        }
    }

    public Notification getNotificationById(Long id) {
        try {
            return notificationRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id " + id));
        } catch (ResourceNotFoundException rnfe) {
            throw rnfe;
        } catch (Exception ex) {
            throw new RuntimeException("Error fetching notification with id " + id, ex);
        }
    }

    public Notification createNotification(Notification notification) {
        try {
            validateNotification(notification);
            return notificationRepository.save(notification);
        } catch (IllegalArgumentException iae) {
            throw iae;
        } catch (Exception ex) {
            throw new RuntimeException("Error creating notification", ex);
        }
    }

    public Notification updateNotification(Long id, Notification notificationDetails) {
        try {
            validateNotification(notificationDetails);
            Notification existing = notificationRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id " + id));
            existing.setReadStatus(notificationDetails.getReadStatus());
            existing.setMessage(notificationDetails.getMessage());
            existing.setReadStatus(notificationDetails.getReadStatus());
            existing.setUser(notificationDetails.getUser());
            return notificationRepository.save(existing);
        } catch (ResourceNotFoundException | IllegalArgumentException e) {
            throw e;
        } catch (Exception ex) {
            throw new RuntimeException("Error updating notification with id " + id, ex);
        }
    }

    public void deleteNotification(Long id) {
        try {
            Notification existing = notificationRepository.findById(id)
                    .orElseThrow(() -> new ResourceNotFoundException("Notification not found with id " + id));
            notificationRepository.delete(existing);
        } catch (ResourceNotFoundException rnfe) {
            throw rnfe;
        } catch (Exception ex) {
            throw new RuntimeException("Error deleting notification with id " + id, ex);
        }
    }

    private void validateNotification(Notification notification) {
        if (notification.getReadStatus() == null || notification.getReadStatus()) {
            throw new IllegalArgumentException("Notification title must not be null or empty");
        }
        if (notification.getMessage() == null || notification.getMessage().trim().isEmpty()) {
            throw new IllegalArgumentException("Notification message must not be null or empty");
        }
    }
}
