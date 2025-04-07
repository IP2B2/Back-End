package com.UAIC.ISMA.controller;

import com.UAIC.ISMA.dto.AuthRequest;
import com.UAIC.ISMA.dto.AuthResponse;
import com.UAIC.ISMA.config.JwtUtil;
import com.UAIC.ISMA.config.PersonDetailsService;
import com.UAIC.ISMA.dao.Person;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PersonDetailsService personDetailsService;

    @PostMapping("/login")
    public ResponseEntity<?> createToken(@RequestBody AuthRequest authRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            authRequest.getUsername(),
                            authRequest.getPassword()
                    )
            );

            // Dacă autentificarea a reușit, extragem UserDetails direct
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();

            // Generăm tokenul
            String token = jwtUtil.generateToken(userDetails,
                    userDetails.getAuthorities().iterator().next().getAuthority());

            return ResponseEntity.ok(new AuthResponse(token));

        } catch (BadCredentialsException ex) {
            // user inexistent sau parolă greșită → 401
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid username or password"));
        } catch (Exception e) {
            // orice altă eroare → 500
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Something went wrong"));
        }
    }

}
