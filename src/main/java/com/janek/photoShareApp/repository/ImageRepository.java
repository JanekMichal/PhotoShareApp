package com.janek.photoShareApp.repository;

import com.janek.photoShareApp.models.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageModel, Long> {
    Optional<ImageModel> findByName(String name);

    @Override
    Optional<ImageModel> findById(Long aLong);

    @Override
    void deleteById(Long aLong);

    void deleteAllByOwnerId(Long ownerId);

    List<ImageModel> findAllByOwnerIdOrderByIdDesc(Long ownerId);

    //TODO: sprawdzić czy to działa
    List<ImageModel> findTop10ByOwnerIdInOrderByIdDesc(Collection<Long> ownerId);
}
