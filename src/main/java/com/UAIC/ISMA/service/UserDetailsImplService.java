package com.UAIC.ISMA.service;

import com.UAIC.ISMA.config.UserDetailsImpl;
import com.UAIC.ISMA.entity.User;
import com.UAIC.ISMA.exception.UserNotFoundException;
import com.UAIC.ISMA.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsImplService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsImplService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail).orElseThrow(() -> new UsernameNotFoundException("User with this email or username not found"));
        return new UserDetailsImpl(user);
    }
}
