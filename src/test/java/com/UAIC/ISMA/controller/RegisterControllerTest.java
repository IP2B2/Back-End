package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.config.JwtUtil;
import com.UAIC.ISMA.entity.Role;
import com.UAIC.ISMA.entity.User;
import com.UAIC.ISMA.entity.enums.RoleName;
import com.UAIC.ISMA.dto.RegistrationRequest;
import com.UAIC.ISMA.dto.ResetPasswordRequest;
import com.UAIC.ISMA.exception.DuplicateEmailException;
import com.UAIC.ISMA.exception.InvalidInputException;
import com.UAIC.ISMA.repository.RoleRepository;
import com.UAIC.ISMA.repository.UserRepository;
import com.UAIC.ISMA.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RegisterControllerTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private EmailService emailService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegisterController registerController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testRegister_Success() {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("john.doe@student.uaic.ro");
        request.setNrMarca("123456");

        when(userRepository.existsByEmail("john.doe@student.uaic.ro")).thenReturn(false);
        when(jwtUtil.generateResetToken(any())).thenReturn("dummy-token");

        Role role = new Role();
        role.setRoleName(RoleName.STUDENT);
        when(roleRepository.findByRoleName(RoleName.STUDENT)).thenReturn(role);

        ResponseEntity<?> response = registerController.register(request);

        assertEquals(201, response.getStatusCodeValue());
        verify(userRepository, times(1)).save(any(User.class));
        verify(emailService, times(1)).sendActivationEmail(eq("john.doe@student.uaic.ro"), eq("dummy-token"));
    }

    @Test
    void testRegister_EmailExists() {
        RegistrationRequest request = new RegistrationRequest();
        request.setEmail("exists@student.uaic.ro");
        request.setNrMarca("123456");

        when(userRepository.existsByEmail("exists@student.uaic.ro")).thenThrow(DuplicateEmailException.class);

    }

    @Test
    void testResetPassword_Success() {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setToken("valid-token");
        request.setNewPassword("newpassword123");

        when(jwtUtil.extractEmailFromResetToken("valid-token")).thenReturn("john.doe@student.uaic.ro");

        User user = new User();
        user.setStatus("inactive");
        when(userRepository.findByEmail("john.doe@student.uaic.ro")).thenReturn(user);
        when(passwordEncoder.encode("newpassword123")).thenReturn("encodedPassword");

        ResponseEntity<?> response = registerController.resetPassword(request);

        assertEquals(200, response.getStatusCodeValue());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testResetPassword_InvalidToken() {
        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setToken("invalid-token");
        request.setNewPassword("password");

        when(jwtUtil.extractEmailFromResetToken("invalid-token")).thenThrow(InvalidInputException.class);

    }
}
