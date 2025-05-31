package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.entity.User;
import com.UAIC.ISMA.entity.enums.RoleName;
import com.UAIC.ISMA.exception.EntityNotFoundException;
import com.UAIC.ISMA.repository.UserRepository;
import com.UAIC.ISMA.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final EmailService emailService;

    public UserController(UserRepository userRepository, EmailService emailService) {
        this.userRepository = userRepository;
        this.emailService = emailService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @PutMapping("/{id}/approve")
    @Operation(
            summary = "Updates user status to active",
            description = "After reviewing the newly created account, an admin approves the user and makes his status active"
    )
    public ResponseEntity<String> approveStudent(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (!"inactive".equalsIgnoreCase(user.getStatus())) {
            return ResponseEntity.badRequest().body("User is already active or not eligible.");
        }

        user.setStatus("active");
        userRepository.save(user);
        emailService.sendAccountApprovedNotification(user.getEmail());

        return ResponseEntity.ok("Account approved and activated.");
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    @Operation(
            summary = "Delete user by ID",
            description = "Allows an admin to permanently delete a user account by ID"
    )
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        userRepository.delete(user);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/user/test")
    public String userTest() {
        return "Hello, User!";
    }

    @GetMapping("/admin/test")
    public String adminTest() {
        return "Hello, Admin!";
    }
}

