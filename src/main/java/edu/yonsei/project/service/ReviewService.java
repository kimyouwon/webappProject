package edu.yonsei.project.service;

import edu.yonsei.project.entity.LikeEntity;
import edu.yonsei.project.entity.ReviewEntity;
import edu.yonsei.project.repository.LikeRepository;
import edu.yonsei.project.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private LikeRepository likeRepository;

    public List<ReviewEntity> getAllReviews() {
        return reviewRepository.findAll();
    }

    public ReviewEntity getReviewById(Long id) {
        return reviewRepository.findById(id).orElseThrow(() -> new RuntimeException("리뷰를 찾을 수 없습니다."));
    }

    public List<ReviewEntity> findByUserId(String userId) {
        return reviewRepository.findByUserId(userId);
    }

    public List<ReviewEntity> getRecentReviews() {
        return reviewRepository.findTop3ByOrderByIdDesc();
    }

    public void updateReview(Long id, String content) {
        ReviewEntity review = getReviewById(id);
        review.setContent(content);
        reviewRepository.save(review);
    }

    public ReviewEntity saveReview(ReviewEntity review) {
        return reviewRepository.save(review);
    }

    @Transactional
    public void deleteReview(Long id) {
        ReviewEntity review = reviewRepository.findById(id).orElse(null);
        if (review != null) {
            List<LikeEntity> likes = likeRepository.findByReview(review);
            likeRepository.deleteAll(likes);
            reviewRepository.deleteById(id);
        }
    }
}
