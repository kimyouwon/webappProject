package edu.yonsei.project.service;

import edu.yonsei.project.entity.CommentEntity;
import edu.yonsei.project.entity.ReviewEntity;
import edu.yonsei.project.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<CommentEntity> getCommentsByReview(ReviewEntity review) {
        return commentRepository.findByReview(review);
    }

    public List<CommentEntity> getCommentsByUserId(String userId) {
        return commentRepository.findByUserId(userId);
    }

    public CommentEntity addComment(CommentEntity comment) {
        return commentRepository.save(comment);
    }
}
