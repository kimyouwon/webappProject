package edu.yonsei.project.controller;

import edu.yonsei.project.entity.ReviewEntity;
import edu.yonsei.project.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @PostMapping("/{reviewId}/edit")
    public String editReview(@PathVariable("reviewId") Long reviewId,
                             @RequestParam("content") String content,
                             HttpSession session, Model model) {
        String userId = (String) session.getAttribute("loginId");
        if (userId == null) {
            return "redirect:/home/login";  // 로그인 페이지로 리다이렉트
        }

        ReviewEntity review = reviewRepository.findById(reviewId).orElse(null);
        if (review != null && review.getUserId().equals(userId)) {
            review.setContent(content);
            reviewRepository.save(review);
        }

        return "redirect:/home_auth/mypage";
    }

    @DeleteMapping("/{reviewId}")
    @ResponseBody
    public String deleteReview(@PathVariable("reviewId") Long reviewId,
                               HttpSession session) {
        String userId = (String) session.getAttribute("loginId");
        if (userId == null) {
            return "redirect:/home/login";  // 로그인 페이지로 리다이렉트
        }

        ReviewEntity review = reviewRepository.findById(reviewId).orElse(null);
        if (review != null && review.getUserId().equals(userId)) {
            reviewRepository.delete(review);
            return "Success";
        }

        return "Failure";
    }
}
