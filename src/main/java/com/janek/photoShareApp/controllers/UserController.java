package com.janek.photoShareApp.controllers;

import com.janek.photoShareApp.models.User;
import com.janek.photoShareApp.payload.response.MessageResponse;
import com.janek.photoShareApp.repository.UserRepository;
import com.janek.photoShareApp.security.services.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class UserController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    UserDetailsServiceImpl userDetailsService;

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> adminAccess() {
        List<User> allUsers = userRepository.findAll();
        return new ResponseEntity<>(allUsers, HttpStatus.OK);
//		return "Admin Board.";
    }

    @GetMapping("/search/{name}")
    public ResponseEntity<?> searchForUser(@PathVariable String name) {
        List<User> searchedUsers = userRepository.findAllByName(name);
        return new ResponseEntity<>(searchedUsers, HttpStatus.OK);
    }

    @GetMapping("/profile/{id}")
    public ResponseEntity<User> getUserProfile(@PathVariable("id") Long id) {
        User retUser = userDetailsService.getUserById(id);
        return new ResponseEntity<>(retUser, HttpStatus.OK);
    }

    @PatchMapping(path = "/profile/{id}/email/{email}")
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

    @DeleteMapping("/admin/delete/username/{username}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> deleteUserByUsername(@PathVariable("username") String username) {
        userDetailsService.deleteUser(username);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Transactional
    @DeleteMapping("/admin/delete/id/{id}")
    public ResponseEntity<?> deleteUserById(@PathVariable("id") Long id) {
        userDetailsService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping(path = "/profile/{id}/{name}")
    public ResponseEntity<User> partialUpdate(@PathVariable Long id, @PathVariable String name) {
        User user = userRepository.findUserById(id);
        user.setName(name);
        userDetailsService.patchUser(user);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PatchMapping(path = "/profile/{id}/username/{userName}")
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


//	@GetMapping("/all")
//	public ResponseEntity<List<User>> allAccess() {
//		List<User> allUsers = userRepository.findAll();
//		return new ResponseEntity<>(allUsers, HttpStatus.OK);
//	}

}
