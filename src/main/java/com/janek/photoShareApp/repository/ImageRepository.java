package com.janek.photoShareApp.repository;

import com.janek.photoShareApp.models.Image;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {
    @Override
    Optional<Image> findById(Long aLong);

    @Override
    void deleteById(Long aLong);

    void deleteAllByOwnerId(Long ownerId);

    List<Image> findAllByOwnerIdOrderByIdDesc(Long ownerId);

    List<Image> findTop10ByOwnerIdInOrderByIdDesc(Collection<Long> ownerId);

    Page<Image> findByOwnerIdInOrderByIdDesc(Collection<Long> ownerId, Pageable pageable);
}
