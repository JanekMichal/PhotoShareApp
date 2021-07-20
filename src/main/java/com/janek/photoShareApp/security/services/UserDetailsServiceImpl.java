package com.janek.photoShareApp.security.services;

import com.janek.photoShareApp.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.janek.photoShareApp.models.User;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	@Autowired
    UserRepository userRepository;

	@Override
	@Transactional
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		User user = userRepository.findByUsernameIgnoreCase(username)
				.orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

		return UserDetailsImpl.build(user);
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
