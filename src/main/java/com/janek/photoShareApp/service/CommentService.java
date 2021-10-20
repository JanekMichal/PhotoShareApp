package com.janek.photoShareApp.service;

import com.janek.photoShareApp.models.Comment;
import com.janek.photoShareApp.models.CommentPage;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
public interface CommentService {
    Page<Comment> getCommentsPage(CommentPage commentPage, Long photoId);
}
