package edu.yonsei.project.service;

import edu.yonsei.project.entity.LikeEntity;
import edu.yonsei.project.entity.ReviewEntity;
import edu.yonsei.project.repository.LikeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LikeService {

    @Autowired
    private LikeRepository likeRepository;

    public long getLikeCountByReview(ReviewEntity review) {
        return likeRepository.countByReview(review);
    }

    public boolean addLike(ReviewEntity review, String userId) {
        if (!likeRepository.existsByReviewAndUserId(review, userId)) {
            LikeEntity like = LikeEntity.builder()
                    .userId(userId)
                    .review(review)
                    .build();
            likeRepository.save(like);
            return true;
        }
        return false;
    }

    public boolean isReviewLikedByUser(ReviewEntity review, String userId) {
        return likeRepository.existsByReviewAndUserId(review, userId);
    }
}
