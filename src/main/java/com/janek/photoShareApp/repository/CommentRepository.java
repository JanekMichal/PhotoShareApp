package com.janek.photoShareApp.repository;

import com.janek.photoShareApp.models.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> getCommentByPhotoId(Long photoId);

    Long countAllByPhotoId(Long photoId);
}
