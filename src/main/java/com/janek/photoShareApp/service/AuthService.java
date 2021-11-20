package com.janek.photoShareApp.service;

import com.janek.photoShareApp.models.Role;
import com.janek.photoShareApp.models.User;
import com.janek.photoShareApp.payload.request.LoginRequest;
import com.janek.photoShareApp.payload.request.SignupRequest;
import com.janek.photoShareApp.payload.response.JwtResponse;
import com.janek.photoShareApp.payload.response.MessageResponse;
import com.janek.photoShareApp.repository.UserRepository;
import com.janek.photoShareApp.security.jwt.JwtUtils;
import lombok.AllArgsConstructor;
import lombok.var;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService {

    UserRepository userRepository;
    JwtUtils jwtUtils;
    AuthenticationManager authenticationManager;
    PasswordEncoder encoder;

    public User getCurrentUser() {
        var principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + principal.getUsername()));
    }

    public ResponseEntity<?> authenticateUser(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken();

        User userDetails = getCurrentUser();

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getEmail(),
                userDetails.getName(),
                userDetails.getRole().toString()
        ));
    }

    public ResponseEntity<?> registerUser(SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already in use!"));
        }

        User newUser = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                Role.USER);

        userRepository.save(newUser);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }
}
