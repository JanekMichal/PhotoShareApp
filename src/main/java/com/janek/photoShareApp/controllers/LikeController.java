package com.janek.photoShareApp.controllers;

import com.janek.photoShareApp.payload.request.LikeRequest;
import com.janek.photoShareApp.service.LikeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/like")
public class LikeController {

    @Autowired
    LikeService likeService;

    //    @PreAuthorize("hasRole('USER')or hasRole('MODERATOR') or hasRole('ADMIN')")
    @PostMapping("/add_like")
    //    @PreAuthorize("hasRole('USER')or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> addLike(@RequestBody LikeRequest likeRequest) {
        return likeService.addLike(likeRequest);
    }

    @Transactional
    @DeleteMapping("/delete_like")
    public ResponseEntity<?> deleteLike(@RequestBody LikeRequest likeRequest) {
        return likeService.deleteLike(likeRequest);
    }

    //    @PreAuthorize("hasRole('USER')or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/likes_count/{image_id}")
    public ResponseEntity<?> getLikesCount(@PathVariable("image_id") Long imageId) {
        return likeService.getLikesCount(imageId);
    }

    @GetMapping("/is_liking_this_image/{image_id}")
    //    @PreAuthorize("hasRole('USER')or hasRole('MODERATOR') or hasRole('ADMIN')")
    public ResponseEntity<?> isLikingThisImage(@PathVariable("image_id") Long imageId) {
        return likeService.isLikingThisImage(imageId);
    }
}
