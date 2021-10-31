package com.janek.photoShareApp.service.implementation;

import com.janek.photoShareApp.models.Comment;
import com.janek.photoShareApp.models.CommentPage;
import com.janek.photoShareApp.repository.CommentRepository;
import com.janek.photoShareApp.service.CommentService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    public CommentServiceImpl(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    @Override
    public Page<Comment> getCommentsPage(CommentPage commentPage, Long photoId) {

        Sort sort = Sort.by(commentPage.getSortDirection(), commentPage.getSortBy());

        Pageable pageable = PageRequest.of(commentPage.getPageNumber(),
                commentPage.getPageSize(), sort);

        return commentRepository.getCommentsByImageId(photoId, pageable);
    }
}
