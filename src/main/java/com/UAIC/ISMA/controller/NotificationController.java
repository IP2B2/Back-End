package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.NotificationDTO;
import com.UAIC.ISMA.exception.NotificationNotFoundException;
import com.UAIC.ISMA.exception.UserNotFoundException;
import com.UAIC.ISMA.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/notifications")
@Tag(name = "Notifications", description = "Operations related to notifications")
public class NotificationController {

    private final NotificationService notificationService;

    public NotificationController(NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @GetMapping("/all")
    @Operation(
            summary = "Get all notifications",
            description = "Returns a list of all notifications. Optionally, filter by userId."
    )
    public ResponseEntity<List<NotificationDTO>> getAllNotifications(
            @Parameter(description = "Optional user ID to filter notifications")
            @RequestParam(required = false) Long userId) {
        List<NotificationDTO> notifications = notificationService.findAll(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/{id}")
    @Operation(
            summary = "Get notification by ID",
            description = "Returns a single notification by its unique ID."
    )
    public ResponseEntity<NotificationDTO> getNotificationById(
            @Parameter(description = "Notification ID")
            @PathVariable Long id) {
        NotificationDTO notificationDTO = notificationService.findById(id);
        return ResponseEntity.ok(notificationDTO);
    }

    @PostMapping
    @Operation(
            summary = "Create a new notification",
            description = "Creates a new notification with the provided details."
    )
    public ResponseEntity<NotificationDTO> createNotification(
            @Parameter(description = "Notification data to create")
            @RequestBody NotificationDTO notificationDTO) {
        NotificationDTO createdNotification = notificationService.create(notificationDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdNotification);
    }

    @PutMapping("/{id}")
    @Operation(
            summary = "Update an existing notification",
            description = "Updates the notification with the specified ID."
    )
    public ResponseEntity<NotificationDTO> updateNotification(
            @Parameter(description = "Notification ID")
            @PathVariable Long id,
            @Parameter(description = "Updated notification data")
            @RequestBody NotificationDTO notificationDTO) {
        NotificationDTO updatedNotification = notificationService.update(id, notificationDTO);
        return ResponseEntity.ok(updatedNotification);
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete a notification",
            description = "Deletes the notification with the specified ID."
    )
    public ResponseEntity<Void> deleteNotification(
            @Parameter(description = "Notification ID")
            @PathVariable Long id) {
        notificationService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(NotificationNotFoundException.class)
    public ResponseEntity<String> handleNotificationNotFoundException(NotificationNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }
}
