package com.janek.photoShareApp.controllers;

import com.janek.photoShareApp.models.Comment;
import com.janek.photoShareApp.models.CommentPage;
import com.janek.photoShareApp.repository.CommentRepository;
import com.janek.photoShareApp.service.CommentService;
import com.janek.photoShareApp.service.implementation.CommentServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/comment")
public class CommentController {

    @Autowired
    CommentRepository commentRepository;

    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping("/add_comment/{photoId}/{creatorId}")
    public ResponseEntity<?> addComment(
            @PathVariable("photoId") Long photoId,
            @PathVariable("creatorId") Long createdBy,
            @RequestBody String description) {

        Comment comment = new Comment(description, createdBy, photoId);

        commentRepository.save(comment);

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/get_comments_paged/{photo_id}/{page}")
    public ResponseEntity<List<Comment>> getCommentsPaged(
            @PathVariable("photo_id") Long photoId,
            @PathVariable("page") int page,
            CommentPage commentPage) {

        commentPage.setPageNumber(page);
        List<Comment> commentsListPaged = commentService.getCommentsPage(commentPage, photoId).getContent();
        return new ResponseEntity<>(commentsListPaged, HttpStatus.OK);
    }

    @GetMapping("/get_comments/{photo_id}")
    public ResponseEntity<List<Comment>> getComments(@PathVariable("photo_id") Long photoId) {
        List<Comment> commentsList = commentRepository.getCommentsByPhotoIdOrderByCommentDateDesc(photoId);

        return new ResponseEntity<>(commentsList, HttpStatus.OK);
    }

    @DeleteMapping("/delete_comment/{comment_id}")
    public ResponseEntity<?> deleteComment(@PathVariable("comment_id") Long photoId) {
        commentRepository.deleteById(photoId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/comments_count/{photo_id}")
    public ResponseEntity<Long> getCommentsCount(@PathVariable("photo_id") Long photoId) {
        Long commentsCount = commentRepository.countAllByPhotoIdOrderById(photoId);

        return new ResponseEntity<>(commentsCount, HttpStatus.OK);
    }

}
