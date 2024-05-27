package edu.yonsei.project.service;

import edu.yonsei.project.entity.ReviewEntity;
import edu.yonsei.project.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public ReviewEntity saveReview(ReviewEntity review) {
        return reviewRepository.save(review);
    }
    public List<ReviewEntity> findByUserId(String userId) {
        return reviewRepository.findByUserId(userId);
    }

}
