package com.janek.photoShareApp.service;

import com.janek.photoShareApp.models.Like;
import com.janek.photoShareApp.models.LikePage;
import com.janek.photoShareApp.payload.request.LikeRequest;
import com.janek.photoShareApp.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private AuthService authService;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public Page<Like> getLikesPage(LikePage likePage, Long imageId) {
        Sort sort = Sort.by(likePage.getSortDirection(), likePage.getSortBy());
        Pageable pageable = PageRequest.of(likePage.getPageNumber(),
                likePage.getPageSize(), sort);

        return likeRepository.getLikesByImageId(imageId, pageable);
    }

    public ResponseEntity<?> addLike(LikeRequest likeRequest) {
        Like like = new Like(authService.getCurrentUser().getId(), likeRequest.getImageId());
        if (!likeRepository.exists(Example.of(like))) {
            likeRepository.save(like);
        }
        return new ResponseEntity<>(like, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteLike( LikeRequest likeRequest) {
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
