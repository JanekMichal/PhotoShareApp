package com.janek.photoShareApp.controllers;

import com.janek.photoShareApp.models.Comment;
import com.janek.photoShareApp.models.CommentPage;
import com.janek.photoShareApp.service.CommentService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/comment")
@AllArgsConstructor
public class CommentController {

    CommentService commentService;

    @PostMapping("/add_comment/{photoId}")
added    public ResponseEntity<Comment> addComment(
            @PathVariable("photoId") Long photoId,
            @RequestBody String commentBody) {
        return commentService.addComment(photoId, commentBody);
    }

    @GetMapping("/get_comments_paged/{photo_id}/{page}")
    public ResponseEntity<List<Comment>> getCommentsPaged(
            @PathVariable("photo_id") Long photoId,
            @PathVariable("page") int page,
            CommentPage commentPage) {
        return commentService.getCommentsPaged(photoId, page, commentPage);
    }

    @GetMapping("/get_comments/{photo_id}")
    public ResponseEntity<List<Comment>> getAllComments(@PathVariable("photo_id") Long photoId) {
        return commentService.getAllComments(photoId);
    }

    @DeleteMapping("/delete_comment/{comment_id}")
    public ResponseEntity<?> deleteComment(@PathVariable("comment_id") Long commentId) {
        return commentService.deleteComment(commentId);
    }

    @GetMapping("/comments_count/{photo_id}")
    public ResponseEntity<Long> getCommentsCount(@PathVariable("photo_id") Long photoId) {
        return commentService.getCommentsCount(photoId);
    }
}
