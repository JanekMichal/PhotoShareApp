package com.janek.photoShareApp.repository;

import com.janek.photoShareApp.models.ImageModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ImageRepository extends JpaRepository<ImageModel, Long> {
    Optional<ImageModel> findByName(String name);

    @Override
    Optional<ImageModel> findById(Long aLong);

    @Override
    void deleteById(Long aLong);

    //    @Query("SELECT a FROM image_table a WHERE owner_id = ?1")
//    List<ImageModel> findAllUserPhotos(Long owner_id);
    List<ImageModel> findAllByOwnerId(Long ownerId);

}
