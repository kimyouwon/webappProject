package edu.yonsei.project.service;

import edu.yonsei.project.entity.CommentEntity;
import edu.yonsei.project.entity.ReviewEntity;
import edu.yonsei.project.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {

    @Autowired
    private CommentRepository commentRepository;

    public List<CommentEntity> getCommentsByReview(ReviewEntity review) {
        return commentRepository.findByReview(review);
    }

    public CommentEntity getCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(() -> new RuntimeException("댓글을 찾을 수 없습니다."));
    }

    public void updateComment(Long id, String content) {
        CommentEntity comment = getCommentById(id);
        comment.setContent(content);
        commentRepository.save(comment);
    }

    public List<CommentEntity> getCommentsByUserId(String userId) {
        return commentRepository.findByUserId(userId);
    }

    public CommentEntity addComment(CommentEntity comment) {
        comment.setCreatedAt(LocalDateTime.now()); // 작성 날짜 설정
        return commentRepository.save(comment);
    }

    public void deleteComment(Long id) {
        CommentEntity comment = commentRepository.findById(id).orElse(null);
        if (comment != null) {
            commentRepository.deleteById(id);
        }
    }
}
