package com.janek.photoShareApp.service;

import com.janek.photoShareApp.models.Like;
import com.janek.photoShareApp.models.LikePage;
import com.janek.photoShareApp.repository.LikeRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class LikeService {

    private final LikeRepository likeRepository;

    public LikeService(LikeRepository likeRepository) {
        this.likeRepository = likeRepository;
    }

    public Page<Like> getLikesPage(LikePage likePage, Long imageId) {
        Sort sort = Sort.by(likePage.getSortDirection(), likePage.getSortBy());
        Pageable pageable = PageRequest.of(likePage.getPageNumber(),
                likePage.getPageSize(), sort);

        return likeRepository.getLikesByImageId(imageId, pageable);
    }
}
