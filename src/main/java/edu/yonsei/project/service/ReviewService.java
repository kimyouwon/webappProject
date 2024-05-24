package edu.yonsei.project.service;

import edu.yonsei.project.entity.ReviewEntity;
import edu.yonsei.project.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    public ReviewEntity saveReview(ReviewEntity review) {
        return reviewRepository.save(review);
    }
}
