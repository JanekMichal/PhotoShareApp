package com.janek.photoShareApp.security.services;

import com.janek.photoShareApp.models.User;
import com.janek.photoShareApp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.Collections.singletonList;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    UserRepository userRepository;


    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(), user.getPassword(), true, true, true,
                true, getAuthorities(user));
    }

    private List<SimpleGrantedAuthority> getAuthorities(User user) {
        return singletonList(new SimpleGrantedAuthority(user.getRole().toString()));

//                user
//                .getRole()
//                new SimpleGrantedAuthority(role
//                        .getName()
//                        .name()))
//                .collect(Collectors.toList());
    }

    public User updateUser(User user) {
        return userRepository.save(user);
    }

    public User patchUser(User user) {
        return userRepository.save(user);
    }

    public void deleteUser(String username) {
        userRepository.deleteUserByUsername(username);
    }

    public void deleteUserById(Long id) {
        userRepository.deleteUserById(id);
    }

    public User getUserById(Long id) {
        return userRepository.findUserById(id);
    }
}
