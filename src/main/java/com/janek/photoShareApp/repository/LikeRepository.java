package com.janek.photoShareApp.repository;

import com.janek.photoShareApp.models.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeRepository extends JpaRepository<Like, Long> {

    int countAllByImageId(Long id);

    Page<Like> getLikesByImageId(Long imageId, Pageable pageable);

    void deleteByImageIdAndOwnerId(Long imageId, Long ownerId);
}
