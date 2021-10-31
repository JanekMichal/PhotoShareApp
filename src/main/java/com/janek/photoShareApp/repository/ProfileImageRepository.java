package com.janek.photoShareApp.repository;

import com.janek.photoShareApp.models.ProfileImage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProfileImageRepository extends JpaRepository<ProfileImage, Long> {

    Optional<ProfileImage> findById(Long id);
    Optional<ProfileImage> getByOwnerId(Long id);
    boolean existsByOwnerId(Long id);
    void deleteByOwnerId(Long id);
}
