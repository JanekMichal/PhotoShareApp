package com.janek.photoShareApp.controllers;

import com.janek.photoShareApp.models.ERole;
import com.janek.photoShareApp.models.User;
import com.janek.photoShareApp.models.UserPage;
import com.janek.photoShareApp.payload.request.UserDataUpdateRequest;
import com.janek.photoShareApp.payload.request.UserPasswordUpdateRequest;
import com.janek.photoShareApp.payload.response.MessageResponse;
import com.janek.photoShareApp.repository.ImageRepository;
import com.janek.photoShareApp.repository.UserRepository;
import com.janek.photoShareApp.security.services.UserDetailsServiceImpl;
import com.janek.photoShareApp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test/")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @Autowired
    ImageRepository imageRepository;

    @Autowired
    PasswordEncoder encoder;

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("page_users/{page}")
    //@PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getUsersPage(@PathVariable("page") int pageNumber, UserPage userPage) {
        userPage.setPageNumber(pageNumber);
        List<User> usersPageList = userService.getUsersPage(userPage).getContent();
        return new ResponseEntity<>(usersPageList, HttpStatus.OK);
    }

    @GetMapping("admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> adminAccess() {
        List<User> allUsers = userRepository.findAll();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
    }

    @GetMapping("user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }


    @GetMapping("search/{name}")
    @PreAuthorize("hasRole('USER')or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> searchForUser(@PathVariable String name) {
        List<User> searchedUsers = userRepository.findAllByUsernameIgnoreCase(name);
        return new ResponseEntity<>(searchedUsers, HttpStatus.OK);
    }

    @GetMapping("profile/{id}")
    @PreAuthorize("hasRole('USER')or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<User> getUserProfile(@PathVariable("id") Long id) {
        User retUser = userDetailsService.getUserById(id);
        return new ResponseEntity<>(retUser, HttpStatus.OK);
    }

    @PatchMapping(path = "profile/{id}/email/{email}")
    @PreAuthorize("hasRole('USER')or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateEmail(@PathVariable Long id, @PathVariable String email) {
        User user = userRepository.findUserById(id);
        if (userRepository.existsByEmail(email) && !user.getEmail().equals(email)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Email is already taken!"));
        }
        user.setEmail(email);
        userDetailsService.patchUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @DeleteMapping("admin/delete/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUserByUsername(@PathVariable("username") String username) {
        userDetailsService.deleteUser(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("delete_user/{id}")
    //@PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteUser(@PathVariable("id") Long userId) {
        userRepository.deleteUserById(userId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("admin/delete/id/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") Long id) {
        imageRepository.deleteAllByOwnerId(id);
        userDetailsService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(path = "profile/{id}/{name}")
    @PreAuthorize("hasRole('USER')or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<User> partialUpdate(@PathVariable Long id, @PathVariable String name) {
        User user = userRepository.findUserById(id);
        user.setName(name);
        userDetailsService.patchUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping(path = "profile/{id}/username/{userName}")
    @PreAuthorize("hasRole('USER')or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateUserName(@PathVariable Long id, @PathVariable String userName) {
        User user = userRepository.findUserById(id);
        if (userRepository.existsByUsername(userName) && !user.getUsername().equals(userName)) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is already taken!"));
        }
        user.setUsername(userName);
        userDetailsService.patchUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping(path = "update_user_data/{id}")
    //@PreAuthorize("hasRole('USER')or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> updateUserData(@PathVariable(name = "id") Long userId,
                                            @RequestBody UserDataUpdateRequest userDataUpdateRequest) {
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


        //userDetailsService.patchUser(user);
        userRepository.save(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Transactional
    @PatchMapping(path = "change_user_password/{id}")
    //@PreAuthorize("hasRole('USER')or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> changeUserPassword(@PathVariable(name = "id") Long userId,
                                                @RequestBody UserPasswordUpdateRequest userPasswordUpdateRequest) {

        User user = userRepository.findUserById(userId);
        user.setPassword(encoder.encode(userPasswordUpdateRequest.getPassword()));
        userDetailsService.patchUser(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Transactional
    @PatchMapping("give_moderator_role/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> giveModeratorRole(@PathVariable("id") Long userId) {

        User user = userRepository.findUserById(userId);
        user.setRole(ERole.ROLE_MODERATOR);
        userRepository.save(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Transactional
    @PatchMapping("give_admin_role/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> giveAdminRole(@PathVariable("id") Long userId) {

        User user = userRepository.findUserById(userId);
        user.setRole(ERole.ROLE_ADMIN);
        userRepository.save(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @Transactional
    @PatchMapping("give_user_role/{id}")
//    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> giveUserRole(@PathVariable("id") Long userId) {

        User user = userRepository.findUserById(userId);
        user.setRole(ERole.ROLE_USER);
        userRepository.save(user);

        return new ResponseEntity<>(user, HttpStatus.OK);
    }
}
