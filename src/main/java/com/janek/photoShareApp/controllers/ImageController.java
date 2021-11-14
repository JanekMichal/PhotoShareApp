package com.janek.photoShareApp.controllers;

import com.janek.photoShareApp.models.Image;
import com.janek.photoShareApp.models.ProfileImage;
import com.janek.photoShareApp.service.ImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "image")
public class ImageController {

    @Autowired
    ImageService imageService;

    @GetMapping("/get_feed_images")
    public ResponseEntity<?> getFeedImages() {
        return imageService.getFeedImages();
    }

    @PatchMapping("/change_description/{image_id}")
    public ResponseEntity<?> changeImageDescription(@RequestBody String description,
                                                    @PathVariable("image_id") Long imageId) {
        return imageService.changeImageDescription(description, imageId);
    }

    @PostMapping("/upload_image")
    public ResponseEntity<?> uploadImage(
            @RequestParam("imageFile") MultipartFile file) throws IOException {
        return imageService.uploadImage(file);
    }

    @GetMapping(path = {"/get/{id}"})
    public Image getImageById(@PathVariable("id") Long imageId) {
        return imageService.getImageById(imageId);
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> deleteImageById(@PathVariable("id") Long imageId) {
        return imageService.deleteImageById(imageId);
    }

    @GetMapping(path = {"/get/all_images/{id}"})
    public ResponseEntity<List<Image>> getAllImages(@PathVariable("id") Long userId) {
        return imageService.getAllImages(userId);
    }

    @Transactional
    @PostMapping("/upload_profile_image")
    public ResponseEntity<?> uploadProfileImage(
            @RequestParam("imageFile") MultipartFile file) throws IOException {
        return imageService.uploadProfileImage(file);
    }

    @GetMapping("/get_profile_image/{user_id}")
    public ProfileImage getProfileImageById(@PathVariable("user_id") Long userId) {
        return imageService.getProfileImageById(userId);
    }
}
