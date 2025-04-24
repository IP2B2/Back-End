package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dao.Role;
import com.UAIC.ISMA.dao.User;
import com.UAIC.ISMA.dao.enums.RoleName;
import com.UAIC.ISMA.dto.RegisterRequest;
import com.UAIC.ISMA.dto.RegisterResponse;
import com.UAIC.ISMA.repository.RoleRepository;
import com.UAIC.ISMA.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*") // IMPORTANT dacă testezi cu Postman sau din altă origine
public class RegisterController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public ResponseEntity<RegisterResponse> registerUser(@RequestBody RegisterRequest request) {

        // VALIDARE username/email
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity
                    .badRequest()
                    .body(new RegisterResponse("Username already taken!"));
        }



        // CREARE user
        User user = new User();
        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        // Atribuire rol
        Role roleAdmin = roleRepository.findByRoleName(RoleName.ADMIN)
                .orElseThrow(() -> new RuntimeException("ADMIN role not found in DB"));

        user.setRole(roleAdmin);

        userRepository.save(user);

        return ResponseEntity.ok(new RegisterResponse("User registered successfully with ADMIN role!"));
    }
}
