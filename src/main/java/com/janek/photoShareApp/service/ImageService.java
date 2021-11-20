package com.janek.photoShareApp.service;

import com.janek.photoShareApp.models.Follow;
import com.janek.photoShareApp.models.Image;
import com.janek.photoShareApp.models.ProfileImage;
import com.janek.photoShareApp.models.User;
import com.janek.photoShareApp.payload.response.MessageResponse;
import com.janek.photoShareApp.repository.FollowRepository;
import com.janek.photoShareApp.repository.ImageRepository;
import com.janek.photoShareApp.repository.ProfileImageRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.janek.photoShareApp.service.ImageCompression.compressBytes;
import static com.janek.photoShareApp.service.ImageCompression.decompressBytes;

@Service
@AllArgsConstructor
public class ImageService {

    ImageRepository imageRepository;
    FollowRepository followRepository;
    ProfileImageRepository profileImageRepository;
    AuthService authService;


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

    public ResponseEntity<?> changeImageDescription(String description, Long imageId) {
        Optional<Image> imageModel = imageRepository.findById(imageId);
        if (imageModel.isPresent()) {
            imageModel.get().setDescription(description);
            imageRepository.save(imageModel.get());
            return new ResponseEntity<>(imageModel, HttpStatus.OK);
        } else {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("We couldn't find this image in our database :("));
        }
    }

    public ResponseEntity<?> uploadImage(MultipartFile file) throws IOException {
        int maxImageSize = 5242880;
        String[] acceptedImageTypes = new String[]{"image/jpeg", "image/jpg", "image/png"};
        if (Arrays.stream(acceptedImageTypes).noneMatch(
                imageType -> imageType.equals(file.getContentType()))) {
            return new ResponseEntity<>("Wrong file type! Only JPG, JPEG and PNG supported.", HttpStatus.UNSUPPORTED_MEDIA_TYPE);
        } else if (file.getSize() > maxImageSize) {
            return new ResponseEntity<>("Image Size is too big! Max size is 5MB.", HttpStatus.PAYLOAD_TOO_LARGE);
        } else {
            Image img = new Image(file.getOriginalFilename(), file.getContentType(),
                    authService.getCurrentUser().getId(), compressBytes(file.getBytes()));
            imageRepository.save(img);
            img.setPicByte(decompressBytes(img.getPicByte()));
            //TODO: fron jest nieprzyzwyczajony do tego, że dostaje jakąś odpowedź
            return new ResponseEntity<>(img, HttpStatus.OK);
        }
    }

    public Image getImageById(Long imageId) {
        final Optional<Image> retrievedImage = imageRepository.findById(imageId);
        return new Image(retrievedImage.get().getName(), retrievedImage.get().getType(),
                retrievedImage.get().getOwnerId(), retrievedImage.get().getDescription(), decompressBytes(retrievedImage.get().getPicByte()));
    }

    public ResponseEntity<?> deleteSomeoneImage(Long imageId) {
        if (imageRepository.existsById(imageId)) {
            imageRepository.deleteById(imageId);
        } else {
            throw new RuntimeException("Image not found!");
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> deleteOwnImage(Long imageId) {
        Image image = imageRepository.findById(imageId).orElseThrow(() -> new RuntimeException("Image not found!"));
        if (image.getOwnerId() != authService.getCurrentUser().getId()) {
            throw new RuntimeException("You are not owner of this image!");
        } else {
            imageRepository.deleteById(imageId);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<List<Image>> getAllImages(Long userId) {
        final List<Image> retrievedImages = imageRepository.findAllByOwnerIdOrderByIdDesc(userId);
        for (Image image : retrievedImages) {
            image.setPicByte(decompressBytes(image.getPicByte()));
        }
        return new ResponseEntity<>(retrievedImages, HttpStatus.OK);
    }

    public ResponseEntity<?> uploadProfileImage(
            MultipartFile file) throws IOException {
        User user = authService.getCurrentUser();
        if (profileImageRepository.existsByOwnerId(user.getId())) {
            profileImageRepository.deleteByOwnerId(user.getId());
        }
        ProfileImage img = new ProfileImage(file.getOriginalFilename(), file.getContentType(),
                user.getId(), compressBytes(file.getBytes()));
        profileImageRepository.save(img);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ProfileImage getProfileImageById(Long userId) {
        final Optional<ProfileImage> retrievedProfileImage = profileImageRepository.getByOwnerId(userId);
        return retrievedProfileImage.map(
                        profileImage -> new ProfileImage(profileImage.getName(), profileImage.getType(),
                                profileImage.getOwnerId(), decompressBytes(profileImage.getPicByte()))).
                orElseThrow(() -> new RuntimeException("Image not found"));
    }
}
