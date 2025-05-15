package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dao.Role;
import com.UAIC.ISMA.dao.User;
import com.UAIC.ISMA.dao.enums.RoleName;
import com.UAIC.ISMA.dto.RegisterRequest;
import com.UAIC.ISMA.dto.RegisterResponse;
import com.UAIC.ISMA.repository.RoleRepository;
import com.UAIC.ISMA.repository.UserRepository;
import com.UAIC.ISMA.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RegisterControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private RegisterController registerController;

    private RegisterRequest request;
    private Role studentRole;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        request = new RegisterRequest();
        request.setUsername("testuser");
        request.setEmail("test@uaic.ro");
        request.setPassword("password123");

        studentRole = new Role();
        studentRole.setId(1L);
        studentRole.setRoleName(RoleName.STUDENT);
    }

    @Test
    void testRegisterUser_Success() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(roleRepository.findByRoleName(RoleName.STUDENT)).thenReturn(Optional.of(studentRole));
        when(passwordEncoder.encode("password123")).thenReturn("hashedPassword");

        ResponseEntity<RegisterResponse> response = registerController.registerUser(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("User registered successfully with ADMIN role!", response.getBody().getMessage());

        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendEmail(eq("test@uaic.ro"), anyString(), anyString());
    }

    @Test
    void testRegisterUser_UsernameAlreadyTaken() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.of(new User()));

        ResponseEntity<RegisterResponse> response = registerController.registerUser(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Username already taken!", response.getBody().getMessage());

        verify(userRepository, never()).save(any());
        verify(emailService, never()).sendEmail(any(), any(), any());
    }

    @Test
    void testRegisterUser_PasswordIsHashed() {
        when(userRepository.findByUsername("testuser")).thenReturn(Optional.empty());
        when(roleRepository.findByRoleName(RoleName.STUDENT)).thenReturn(Optional.of(studentRole));
        when(passwordEncoder.encode("password123")).thenReturn("bcryptHashedPass");

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        registerController.registerUser(request);

        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertNotEquals("password123", savedUser.getPassword());
        assertEquals("bcryptHashedPass", savedUser.getPassword());
    }
}
