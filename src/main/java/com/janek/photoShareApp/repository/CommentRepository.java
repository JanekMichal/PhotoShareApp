package com.janek.photoShareApp.repository;

import com.janek.photoShareApp.models.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {
    List<Comment> getCommentByImageId(Long photoId);

    Long countAllByImageIdOrderById(Long photoId);

    Page<Comment> getCommentsByImageId(Long photoId, Pageable pageable);

    List<Comment> getCommentsByImageIdOrderByCommentDateDesc(Long photoId);

    Comment findCommentById(Long commentId);
}
