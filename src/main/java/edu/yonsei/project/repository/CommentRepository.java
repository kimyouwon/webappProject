package edu.yonsei.project.repository;

import edu.yonsei.project.entity.CommentEntity;
import edu.yonsei.project.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Long> {
    List<CommentEntity> findByReview(ReviewEntity review);
    List<CommentEntity> findByUserId(String userId);
}
