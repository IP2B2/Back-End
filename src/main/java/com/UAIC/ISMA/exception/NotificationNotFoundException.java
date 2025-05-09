package com.UAIC.ISMA.exception;

public class NotificationNotFoundException extends EntityNotFoundException {
    public NotificationNotFoundException(Long id) {
        super("Notification not found with ID: " + id);
    }
}
