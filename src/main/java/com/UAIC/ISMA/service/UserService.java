package com.UAIC.ISMA.service;

import com.UAIC.ISMA.entity.User;
import com.UAIC.ISMA.exception.UserNotFoundException;
import com.UAIC.ISMA.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Long findIdByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found with username: " + username));
        return user.getId();
    }
}
