package com.janek.photoShareApp.service;

import com.janek.photoShareApp.models.Comment;
import com.janek.photoShareApp.models.CommentPage;
import com.janek.photoShareApp.repository.CommentRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    AuthService authService;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Page<Comment> getCommentsPage(CommentPage commentPage, Long imageId) {
        Sort sort = Sort.by(commentPage.getSortDirection(), commentPage.getSortBy());
        Pageable pageable = PageRequest.of(commentPage.getPageNumber(),
                commentPage.getPageSize(), sort);

        return commentRepository.getCommentsByImageId(imageId, pageable);
    }

    public ResponseEntity<?> addComment(Long photoId, String commentBody) {
        Comment comment = new Comment(commentBody, authService.getCurrentUser().getId(), photoId);
        commentRepository.save(comment);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<List<Comment>> getCommentsPaged(Long photoId, int page, CommentPage commentPage) {
        commentPage.setPageNumber(page);
        List<Comment> commentsListPaged = getCommentsPage(commentPage, photoId).getContent();
        return new ResponseEntity<>(commentsListPaged, HttpStatus.OK);
    }

    public ResponseEntity<List<Comment>> getAllComments(Long photoId) {
        List<Comment> commentsList = commentRepository.getCommentsByImageIdOrderByCommentDateDesc(photoId);
        return new ResponseEntity<>(commentsList, HttpStatus.OK);
    }

    public ResponseEntity<?> deleteComment(Long commentId) {
        commentRepository.deleteById(commentId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public ResponseEntity<Long> getCommentsCount(Long photoId) {
        Long commentsCount = commentRepository.countAllByImageIdOrderById(photoId);
        return new ResponseEntity<>(commentsCount, HttpStatus.OK);
    }
}
