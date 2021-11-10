package com.janek.photoShareApp.service;

import com.janek.photoShareApp.models.User;
import com.janek.photoShareApp.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.var;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class AuthService {

    private UserRepository userRepository;

    public User getCurrentUser() {
        var principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext()
                .getAuthentication()
                .getPrincipal();

        return userRepository.findByUsername(principal.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + principal.getUsername()));

    }
}
