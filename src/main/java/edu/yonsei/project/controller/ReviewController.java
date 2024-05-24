package edu.yonsei.project.controller;

import edu.yonsei.project.entity.ReviewEntity;
import edu.yonsei.project.service.ReviewService;
import edu.yonsei.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/review")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String showReviewPage(Model model) {
        model.addAttribute("review", new ReviewEntity());
        return "reviews_test";
    }

    @PostMapping
    public String createReview(@ModelAttribute ReviewEntity review, HttpSession session) {
        String userId = (String) session.getAttribute("loginId");
        if (userId == null) {
            // 사용자 ID가 세션에 없으면 로그인 페이지로 리다이렉트
            return "redirect:/login";
        }

        review.setUserId(userId);
        reviewService.saveReview(review);
        return "redirect:/review";
    }
}
