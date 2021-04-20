package com.janek.photoShareApp.repository;

import java.util.List;
import java.util.Optional;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.janek.photoShareApp.models.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	Optional<User> findByUsername(String username);
	List<User> findAll();
	Boolean existsByUsername(String username);
	Boolean existsByEmail(String email);

	void deleteUserByUsername(String username);
	void deleteUserById(Long id);
	User findUserById(Long id);
}
