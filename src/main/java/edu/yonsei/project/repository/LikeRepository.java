package edu.yonsei.project.repository;

import edu.yonsei.project.entity.LikeEntity;
import edu.yonsei.project.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<LikeEntity, Long> {
    long countByReview(ReviewEntity review);
    boolean existsByReviewAndUserId(ReviewEntity review, String userId);
    LikeEntity findByReviewAndUserId(ReviewEntity review, String userId);
}
