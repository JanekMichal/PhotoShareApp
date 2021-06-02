package com.janek.photoShareApp.controllers;

import com.janek.photoShareApp.models.Follow;
import com.janek.photoShareApp.models.ImageModel;
import com.janek.photoShareApp.payload.response.MessageResponse;
import com.janek.photoShareApp.repository.FollowRepository;
import com.janek.photoShareApp.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;


@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping(path = "image")
public class ImageController {
    @Autowired
    ImageRepository imageRepository;

    @Autowired
    FollowRepository followRepository;

    @GetMapping("/get_feed_photos/{userId}")
    public ResponseEntity<?> getFeedImages(@PathVariable("userId") Long userId) {
        List<Follow> listOfAllFollowedUsers = followRepository.findAllByFollowerId(userId);
        List<Long> listOfIdAllFollowedUsers = new ArrayList<>();
        for (Follow follow : listOfAllFollowedUsers) {
            listOfIdAllFollowedUsers.add(follow.getFollowing().getId());
            //System.out.println(follow.getFollowing().getId());
        }

        final List<ImageModel> retrievedImages = imageRepository.findTop10ByOwnerIdInOrderByIdDesc(listOfIdAllFollowedUsers);

        if (retrievedImages != null) {
            for (ImageModel imageModel : retrievedImages) {
                imageModel.setPicByte(decompressBytes(imageModel.getPicByte()));
                //imageModel.setPicByte(null);
                //imageModel.setOwnerId(1L);
            }
            return new ResponseEntity<>(retrievedImages, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(2, HttpStatus.NOT_FOUND);
        }
    }

//    @PutMapping("/change_description")
//    public ResponseEntity<?> changeDescription(@RequestBody ImageModel changedDescription) {
//        imageRepository.save(changedDescription);
//        return new ResponseEntity<>(HttpStatus.OK);
//    }

    @PatchMapping("/change_description/{imageId}")
    public ResponseEntity<?> changeDescription(@RequestBody String description2,
                                               @PathVariable("imageId") Long imageId
                                               ) {

        Optional<ImageModel> imageModel = imageRepository.findById(imageId);
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


//    @PatchMapping(path = "/profile/{id}/username/{userName}")
//    public ResponseEntity<?> updateUserName(@PathVariable Long id, @PathVariable String userName) {
//        User user = userRepository.findUserById(id);
//        if (userRepository.existsByUsername(userName) && !user.getUsername().equals(userName)) {
//            return ResponseEntity
//                    .badRequest()
//                    .body(new MessageResponse("Error: Username is already taken!"));
//        }
//        user.setUsername(userName);
//        userDetailsService.patchUser(user);
//        return new ResponseEntity<>(user, HttpStatus.OK);
//    }

    @PostMapping("/upload/{userId}")
    public ResponseEntity<?> uploadImage(@RequestParam("imageFile") MultipartFile file, @PathVariable("userId") Long userId) throws IOException {
        System.out.println("Original Image Byte Size - " + file.getBytes().length);
        ImageModel img = new ImageModel(file.getOriginalFilename(), file.getContentType(),
                compressBytes(file.getBytes()), userId);
        imageRepository.save(img);
        return new ResponseEntity<>(HttpStatus.OK);
    }

//    @GetMapping(path = {"/get/{imageName}"})
//    public ImageModel getImage(@PathVariable("imageName") String imageName) {
//        final Optional<ImageModel> retrievedImage = imageRepository.findByName(imageName);
//        return new ImageModel(retrievedImage.get().getName(), retrievedImage.get().getType(),
//                decompressBytes(retrievedImage.get().getPicByte()), retrievedImage.get().getOwnerId());
//    }

    @GetMapping(path = {"/get/{id}"})
    public ImageModel getImageById(@PathVariable("id") Long id) {
        final Optional<ImageModel> retrievedImage = imageRepository.findById(id);
        return new ImageModel(retrievedImage.get().getName(), retrievedImage.get().getType(),
                decompressBytes(retrievedImage.get().getPicByte()), retrievedImage.get().getOwnerId());
    }

    @DeleteMapping(path = "/delete/{id}")
    public ResponseEntity<?> deletePhotoById(@PathVariable("id") Long id) {
        imageRepository.deleteById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping(path = {"/get/allphotos/{id}"})
    public ResponseEntity<List<ImageModel>> getAllImages(@PathVariable("id") Long id) {
        final List<ImageModel> retrievedImages = imageRepository.findAllByOwnerIdOrderByIdDesc(id);
        for (ImageModel imageModel : retrievedImages) {
            imageModel.setPicByte(decompressBytes(imageModel.getPicByte()));
            //imageModel.setOwnerId(1L);
        }
        return new ResponseEntity<>(retrievedImages, HttpStatus.OK);
    }

    // compress the image bytes before storing it in the database
    public static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException ignored) {
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    // uncompress the image bytes before returning it to the angular application
    public static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException ignored) {
        }
        return outputStream.toByteArray();
    }
}
