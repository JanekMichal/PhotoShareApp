package com.janek.photoShareApp.repository;

import com.janek.photoShareApp.models.Like;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    int countAllByImageId(Long id);
}
