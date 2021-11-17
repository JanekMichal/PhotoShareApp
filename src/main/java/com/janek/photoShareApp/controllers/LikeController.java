package com.janek.photoShareApp.controllers;

import com.janek.photoShareApp.payload.request.LikeRequest;
import com.janek.photoShareApp.service.LikeService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/like")
@AllArgsConstructor
public class LikeController {

    LikeService likeService;

    @PostMapping("/add_like")
    public ResponseEntity<?> addLike(@RequestBody LikeRequest likeRequest) {
        return likeService.addLike(likeRequest);
    }

    @Transactional
    @DeleteMapping("/delete_like")
    public ResponseEntity<?> deleteLike(@RequestBody LikeRequest likeRequest) {
        return likeService.deleteLike(likeRequest);
    }

    @GetMapping("/likes_count/{image_id}")
    public ResponseEntity<?> getLikesCount(@PathVariable("image_id") Long imageId) {
        return likeService.getLikesCount(imageId);
    }

    @GetMapping("/is_liking_this_image/{image_id}")
    public ResponseEntity<?> isLikingThisImage(@PathVariable("image_id") Long imageId) {
        return likeService.isLikingThisImage(imageId);
    }
}
