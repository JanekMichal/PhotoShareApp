package com.janek.photoShareApp.controllers;

import com.janek.photoShareApp.models.Follow;
import com.janek.photoShareApp.models.Image;
import com.janek.photoShareApp.models.ProfileImage;
import com.janek.photoShareApp.payload.response.MessageResponse;
import com.janek.photoShareApp.repository.CommentRepository;
import com.janek.photoShareApp.repository.FollowRepository;
import com.janek.photoShareApp.repository.ImageRepository;
import com.janek.photoShareApp.repository.ProfileImageRepository;
import com.janek.photoShareApp.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.janek.photoShareApp.service.ImageCompression.compressBytes;
import static com.janek.photoShareApp.service.ImageCompression.decompressBytes;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "image")
public class ImageController {
    @Autowired
    ImageRepository imageRepository;

    @Autowired
    FollowRepository followRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ProfileImageRepository profileImageRepository;

    @Autowired
    AuthService authService;

    @GetMapping("/get_feed_images")
    public ResponseEntity<?> getFeedImages() {

        List<Follow> listOfAllFollowedUsers = followRepository.findAllByFollowerId(authService.getCurrentUser().getId());
        List<Long> listOfIdAllFollowedUsers = new ArrayList<>();
        for (Follow follow : listOfAllFollowedUsers) {
            listOfIdAllFollowedUsers.add(follow.getFollowing().getId());

        }

        final List<Image> retrievedImages = imageRepository.findTop10ByOwnerIdInOrderByIdDesc(listOfIdAllFollowedUsers);

        if (retrievedImages != null) {
            for (Image image : retrievedImages) {
                image.setPicByte(decompressBytes(image.getPicByte()));
            }
            return new ResponseEntity<>(retrievedImages, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(2, HttpStatus.NOT_FOUND);
        }
    }

    @PatchMapping("/change_description/{image_id}")
    public ResponseEntity<?> changeDescription(@RequestBody String description2,
                                               @PathVariable("image_id") Long imageId) {
        Optional<Image> imageModel = imageRepository.findById(imageId);
        if (imageModel.isPresent()) {
            imageModel.get().setDescription(description2);
            imageRepository.save(imageModel.get());
            return new ResponseEntity<>(imageModel, HttpStatus.OK);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("We couldn't find this image in our database :("));
        }
    }

    @PostMapping("/upload_image/{user_id}")
    public ResponseEntity<?> uploadImage(
            @RequestParam("imageFile") MultipartFile file,
            @PathVariable("user_id") Long userId) throws IOException {

        Image img = new Image(file.getOriginalFilename(), file.getContentType(),
                userId, compressBytes(file.getBytes()));
        imageRepository.save(img);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = {"/get/{id}"})
    public Image getImageById(@PathVariable("id") Long id) {
        final Optional<Image> retrievedImage = imageRepository.findById(id);
        return new Image(retrievedImage.get().getName(), retrievedImage.get().getType(),
                retrievedImage.get().getOwnerId(), retrievedImage.get().getDescription(), decompressBytes(retrievedImage.get().getPicByte()));
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> deleteImageById(@PathVariable("id") Long id) {
        imageRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = {"/get/all_images/{id}"})
    public ResponseEntity<List<Image>> getAllImages(@PathVariable("id") Long id) {
        final List<Image> retrievedImages = imageRepository.findAllByOwnerIdOrderByIdDesc(id);
        for (Image image : retrievedImages) {
            image.setPicByte(decompressBytes(image.getPicByte()));
        }
        return new ResponseEntity<>(retrievedImages, HttpStatus.OK);
    }

    @Transactional
    @PostMapping("/upload_profile_image/{user_id}")
    public ResponseEntity<?> uploadProfileImage(
            @RequestParam("imageFile") MultipartFile file,
            @PathVariable("user_id") Long userId) throws IOException {

        if (profileImageRepository.existsByOwnerId(userId)) {
            profileImageRepository.deleteByOwnerId(userId);
        }
        ProfileImage img = new ProfileImage(file.getOriginalFilename(), file.getContentType(),
                userId, compressBytes(file.getBytes()));
        profileImageRepository.save(img);


        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get_profile_image/{user_id}")
    public ProfileImage getProfileImageById(@PathVariable("user_id") Long userId) {
        final Optional<ProfileImage> retrievedProfileImage = profileImageRepository.getByOwnerId(userId);

        return retrievedProfileImage.map(
                        profileImage -> new ProfileImage(profileImage.getName(), profileImage.getType(),
                                profileImage.getOwnerId(), decompressBytes(profileImage.getPicByte()))).
                orElseThrow(() -> new RuntimeException("Image not found"));
    }
}
