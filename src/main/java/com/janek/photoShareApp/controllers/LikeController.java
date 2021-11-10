package com.janek.photoShareApp.controllers;

import com.janek.photoShareApp.models.Like;
import com.janek.photoShareApp.payload.request.LikeRequest;
import com.janek.photoShareApp.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/like")
public class LikeController {

    @Autowired
    LikeRepository likeRepository;

    //    @PreAuthorize("hasRole('USER')or hasRole('MODERATOR') or hasRole('ADMIN')")
    @PostMapping("/add_like")
    public ResponseEntity<?> addLike(@RequestBody LikeRequest likeRequest) {
        Like like = new Like(likeRequest.getOwnerId(), likeRequest.getImageId());
        if (!likeRepository.exists(Example.of(like))) {
            likeRepository.save(like);
        }

        return new ResponseEntity<>(like, HttpStatus.OK);
    }

    //    @PreAuthorize("hasRole('USER')or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/likes_count/{image_id}")
    public ResponseEntity<?> getLikesCount(@PathVariable("image_id") Long imageId) {
        int likesCount = likeRepository.countAllByImageId(imageId);

        return new ResponseEntity<>(likesCount, HttpStatus.OK);
    }

    @GetMapping("/get_likes_page/{image_id}/{page_number}")
    public ResponseEntity<?> getLikesPage(@PathVariable("image_id") Long imageId,
                                          @PathVariable("page_number") Long pageNumber) {


        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/is_liking_this_image/{image_id}/{user_id}")
    public ResponseEntity<?> isLikingThisImage(@PathVariable("image_id") Long imageId,
                                               @PathVariable("user_id") Long userId) {
        Like like = new Like(userId, imageId);

        return new ResponseEntity<>(likeRepository.exists(Example.of(like)), HttpStatus.OK);
    }

}
