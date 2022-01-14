package com.janek.photoShareApp.controllers;

import com.janek.photoShareApp.models.Image;
import com.janek.photoShareApp.models.ImagePage;
import com.janek.photoShareApp.service.ImageService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "image")
@AllArgsConstructor
public class ImageController {

    ImageService imageService;

    @GetMapping("/get_feed_images")
    public ResponseEntity<?> getFeedImages() {
        return imageService.getFeedImages();
    }

    @GetMapping("/get_feed_images_paged/{page_number}")
    public ResponseEntity<?> getFeedImages(@PathVariable("page_number") int pageNumber) {
        return imageService.getFeedImagesPaged(pageNumber, new ImagePage());
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

    @DeleteMapping(path = "/delete_someone_image/{id}")
    public ResponseEntity<?> deleteSomeoneImage(@PathVariable("id") Long imageId) {
        return imageService.deleteSomeoneImage(imageId);
    }

    @DeleteMapping(path = "/delete_own_image/{id}")
    public ResponseEntity<?> deleteOwnImage(@PathVariable("id") Long imageId) {
        return imageService.deleteOwnImage(imageId);
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
    public ResponseEntity<?> getProfileImageById(@PathVariable("user_id") Long userId) {
        return imageService.getProfileImageById(userId);
    }
}
