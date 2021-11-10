package com.janek.photoShareApp.service;

import com.janek.photoShareApp.models.Comment;
import com.janek.photoShareApp.models.CommentPage;
import com.janek.photoShareApp.repository.CommentRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public Page<Comment> getCommentsPage(CommentPage commentPage, Long imageId) {
        Sort sort = Sort.by(commentPage.getSortDirection(), commentPage.getSortBy());
        Pageable pageable = PageRequest.of(commentPage.getPageNumber(),
                commentPage.getPageSize(), sort);

        return commentRepository.getCommentsByImageId(imageId, pageable);
    }
}
