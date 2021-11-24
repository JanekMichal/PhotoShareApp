package com.janek.photoShareApp.service;

import com.janek.photoShareApp.models.Role;
import com.janek.photoShareApp.models.User;
import com.janek.photoShareApp.models.UserPage;
import com.janek.photoShareApp.payload.request.UserDataUpdateRequest;
import com.janek.photoShareApp.payload.request.UserPasswordUpdateRequest;
import com.janek.photoShareApp.payload.response.MessageResponse;
import com.janek.photoShareApp.repository.FollowRepository;
import com.janek.photoShareApp.repository.ImageRepository;
import com.janek.photoShareApp.repository.ProfileImageRepository;
import com.janek.photoShareApp.repository.UserRepository;
import com.janek.photoShareApp.security.services.UserDetailsServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@AllArgsConstructor
public class UserService {

    PasswordEncoder encoder;
    UserRepository userRepository;
    UserDetailsServiceImpl userDetailsService;
    FollowRepository followRepository;
    ImageRepository imageRepository;
    AuthService authService;
    ProfileImageRepository profileImageRepository;

    public ResponseEntity<List<User>> getUsersPage(int pageNumber, UserPage userPage) {
        userPage.setPageNumber(pageNumber);
        Sort sort = Sort.by(userPage.getSortDirection(), userPage.getSortBy());
        Pageable pageable = PageRequest.of(userPage.getPageNumber(),
                userPage.getPageSize(), sort);
        return new ResponseEntity<>(userRepository.findAll(pageable).getContent(), HttpStatus.OK);
    }

    public ResponseEntity<?> searchForUser(String name) {
        List<User> searchedUsers = userRepository.findAllByUsernameIgnoreCase(name);
        return new ResponseEntity<>(searchedUsers, HttpStatus.OK);
    }

    public ResponseEntity<User> getUserProfile(Long userId) {
        User user = userDetailsService.getUserById(userId);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

//    public ResponseEntity<?> updateEmail(Long id, String email) {
//        User user = userRepository.findUserById(id);
//        if (userRepository.existsByEmail(email) && !user.getEmail().equals(email)) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Email is already taken!"));
//        }
//        user.setEmail(email);
//        userDetailsService.patchUser(user);
//        return new ResponseEntity<>(user, HttpStatus.OK);
//    }

    public ResponseEntity<?> deleteSomeoneElseAccount(Long userId) {
        if (userRepository.findUserById(userId).getRole() == Role.ADMIN) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        followRepository.deleteAllByFollowerId(userId);
        followRepository.deleteAllByFollowingId(userId);
        userRepository.deleteUserById(userId);
        imageRepository.deleteAllByOwnerId(userId);
        profileImageRepository.deleteByOwnerId(userId);
        
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> deleteOwnAccount(Long userId) {
        if (!Objects.equals(authService.getCurrentUser().getId(), userId)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: You can't delete someone else account!"));
        } else {
            followRepository.deleteAllByFollowerId(userId);
            followRepository.deleteAllByFollowingId(userId);
            userRepository.deleteUserById(userId);
            imageRepository.deleteAllByOwnerId(userId);
            profileImageRepository.deleteByOwnerId(userId);
            return new ResponseEntity<>(HttpStatus.OK);
        }
    }

    public ResponseEntity<?> updateUserData(Long userId, UserDataUpdateRequest userDataUpdateRequest) {
        User user = userRepository.findUserById(userId);

        if (!user.getUsername().equals(userDataUpdateRequest.getUsername())) {
            if (userRepository.existsByUsername(userDataUpdateRequest.getUsername())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Username is already taken!"));
            } else {
                user.setUsername(userDataUpdateRequest.getUsername());
            }
        }

        if (!user.getEmail().equals(userDataUpdateRequest.getEmail())) {
            if (userRepository.existsByEmail(userDataUpdateRequest.getEmail())
                    && !user.getEmail().equals(userDataUpdateRequest.getEmail())) {
                return ResponseEntity
                        .badRequest()
                        .body(new MessageResponse("Error: Email is already in use!"));
            } else {
                user.setEmail(userDataUpdateRequest.getEmail());
            }
        }

        if (!user.getName().equals(userDataUpdateRequest.getName())) {
            user.setName(userDataUpdateRequest.getName());
        }

        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public ResponseEntity<?> changeUserPassword(Long userId, UserPasswordUpdateRequest userPasswordUpdateRequest) {
        User user = userRepository.findUserById(userId);
        user.setPassword(encoder.encode(userPasswordUpdateRequest.getPassword()));
        userDetailsService.patchUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    public ResponseEntity<User> giveRole(Long userId, Role role) {
        User user = userRepository.findUserById(userId);
        user.setRole(role);
        userRepository.save(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
