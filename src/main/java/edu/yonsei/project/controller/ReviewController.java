package edu.yonsei.project.controller;

import edu.yonsei.project.entity.ReviewEntity;
import edu.yonsei.project.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @GetMapping({"/home/exhibition/detail","/home_auth/exhibition/detail"})
    public String showExhibitionDetailPage() {
        return "exhibition_De_page";
    }

    @PostMapping
    public ResponseEntity<ReviewEntity> createReview(@RequestBody ReviewEntity review) {
        ReviewEntity savedReview = reviewService.saveReview(review);
        return ResponseEntity.ok(savedReview);
    }
}
