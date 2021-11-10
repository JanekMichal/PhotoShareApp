package com.janek.photoShareApp.repository;

import com.janek.photoShareApp.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsernameIgnoreCase(String username);

    List<User> findAllByUsernameIgnoreCase(String username);

    List<User> findAll();

    Optional<User> findByUsername(String userName);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    void deleteUserByUsername(String username);

    void deleteUserById(Long id);

    User findUserById(Long id);

    List<User> findAllByName(String name);

    List<User> findByUsernameOrEmail(String userName, String email);
}

