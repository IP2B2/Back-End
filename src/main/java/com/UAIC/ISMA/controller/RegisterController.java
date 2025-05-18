package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.config.JwtUtil;
import com.UAIC.ISMA.dto.ResetPasswordRequest;
import com.UAIC.ISMA.entity.Role;
import com.UAIC.ISMA.entity.User;
import com.UAIC.ISMA.dto.RegistrationRequest;
import com.UAIC.ISMA.entity.enums.RoleName;
import com.UAIC.ISMA.exception.DuplicateEmailException;
import com.UAIC.ISMA.exception.InvalidInputException;
import com.UAIC.ISMA.repository.RoleRepository;
import com.UAIC.ISMA.repository.UserRepository;
import com.UAIC.ISMA.service.EmailService;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class RegisterController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public RegisterController(UserRepository userRepository,
                              RoleRepository roleRepository,
                              EmailService emailService,
                              JwtUtil jwtUtil,
                              PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.emailService = emailService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }


    @PostMapping("/register")
    @Operation(
            summary = "Registers a new user",
            description = "Generates an email that is sent to the specified address where the user can reset his password"
    )
    public ResponseEntity<?> register(@RequestBody RegistrationRequest request) {
        String email = request.getEmail();

        if (!email.endsWith("@student.uaic.ro")) {
            throw new InvalidInputException("Email must end with @student.uaic.ro");
        }

        if (userRepository.existsByEmail(email)) {
            throw new DuplicateEmailException("An account with this email already exists.");
        }

        String[] parts = email.split("@")[0].split("\\.");
        String username = Arrays.stream(parts)
                .map(p -> p.substring(0, 1).toUpperCase() + p.substring(1))
                .collect(Collectors.joining(" "));

        String token = jwtUtil.generateResetToken(email);

        Role studentRole = roleRepository.findByRoleName(RoleName.STUDENT);

        User user = new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(request.getRegistrationNumber());
        user.setRole(studentRole);
        user.setStatus("inactive");

        userRepository.save(user);
        emailService.sendActivationEmail(email, token);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Account created. Please check your email to set your password.");
    }

    @PostMapping("/reset-password")
    @Operation(
            summary = "Creates password",
            description = "The user's new password is stored (hashed) in the database and the account becomes active"
    )
    public ResponseEntity<?> resetPassword(@RequestBody ResetPasswordRequest request) {
        String email;
        try {
            email = jwtUtil.extractEmailFromResetToken(request.getToken());
            System.out.println("Token received: " + request.getToken());
        } catch (Exception e) {
            throw new InvalidInputException("Invalid or expired token.");
        }

        User user = userRepository.findByEmail(email);
        if (user == null || "active".equalsIgnoreCase(user.getStatus())) {
            throw new InvalidInputException("This reset link is not valid for your account.");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setStatus("active");
        userRepository.save(user);

        return ResponseEntity.ok("Password set successfully. You may now log in.");
    }
}
