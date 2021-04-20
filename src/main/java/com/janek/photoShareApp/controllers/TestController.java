package com.janek.photoShareApp.controllers;

import com.janek.photoShareApp.models.User;
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
public class TestController {
	@Autowired
	UserRepository userRepository;

	@Autowired
	UserDetailsServiceImpl userDetailsService;
//	@GetMapping("/all")
//	public String allAccess() {
//		return "Public cokolwiek.";
//	}
	
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
	public ResponseEntity<List<User>>adminAccess() {
		List<User> allUsers = userRepository.findAll();
		return new ResponseEntity<>(allUsers, HttpStatus.OK);
//		return "Admin Board.";
	}

	@PutMapping("/profile/edit-name")
	//@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<User> editName(@RequestBody User user) {
		User updateUser = userDetailsService.updateUser(user);
		return new ResponseEntity<>(updateUser, HttpStatus.OK);
	}

	@DeleteMapping("/admin/delete/username/{username}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteUserByUsername(@PathVariable("username") String username) {
		userDetailsService.deleteUser(username);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@Transactional
	@DeleteMapping("/admin/delete/id/{id}")
	@PreAuthorize("hasRole('ADMIN')")
	public ResponseEntity<?> deleteUserById(@PathVariable("id") Long id) {
		userDetailsService.deleteUserById(id);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/profile/{id}")
	public ResponseEntity<User> getUserProfile(@PathVariable("id") Long id) {
		User retUser = userDetailsService.getUserById(id);
		return new ResponseEntity<>(retUser, HttpStatus.OK);
	}




//	@GetMapping("/all")
//	public ResponseEntity<List<User>> allAccess() {
//		List<User> allUsers = userRepository.findAll();
//		return new ResponseEntity<>(allUsers, HttpStatus.OK);
//	}

}
