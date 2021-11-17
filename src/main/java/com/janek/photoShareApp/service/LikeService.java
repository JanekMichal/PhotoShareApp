package com.janek.photoShareApp.service;

import com.janek.photoShareApp.models.Like;
import com.janek.photoShareApp.payload.request.LikeRequest;
import com.janek.photoShareApp.repository.LikeRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Example;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class LikeService {

    private LikeRepository likeRepository;
    private AuthService authService;

    public ResponseEntity<?> addLike(LikeRequest likeRequest) {
        Like like = new Like(authService.getCurrentUser().getId(), likeRequest.getImageId());
        if (!likeRepository.exists(Example.of(like))) {
            likeRepository.save(like);
        }
        return new ResponseEntity<>(like, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteLike(LikeRequest likeRequest) {
        likeRepository.deleteByImageIdAndOwnerId(likeRequest.getImageId(), authService.getCurrentUser().getId());

        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<?> getLikesCount(Long imageId) {
        int likesCount = likeRepository.countAllByImageId(imageId);

        return new ResponseEntity<>(likesCount, HttpStatus.OK);
    }

    public ResponseEntity<?> isLikingThisImage(Long imageId) {
        Like like = new Like(authService.getCurrentUser().getId(), imageId);

        return new ResponseEntity<>(likeRepository.exists(Example.of(like)), HttpStatus.OK);
    }
}
