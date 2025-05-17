package com.UAIC.ISMA.service;

import com.UAIC.ISMA.entity.User;
import com.UAIC.ISMA.exception.UserNotFoundException;
import com.UAIC.ISMA.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    private UserRepository userRepository;
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository = mock(UserRepository.class);
        userService = new UserService(userRepository);
    }

    @Test
    void testFindIdByUsername_found() {
        User user = new User();
        user.setId(99L);
        when(userRepository.findByUsername("admin")).thenReturn(Optional.of(user));

        Long id = userService.findIdByUsername("admin");
        assertEquals(99L, id);
    }

    @Test
    void testFindIdByUsername_notFound() {
        when(userRepository.findByUsername("ghost")).thenReturn(Optional.empty());
        assertThrows(UserNotFoundException.class, () -> userService.findIdByUsername("ghost"));
    }
}
