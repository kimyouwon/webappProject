package edu.yonsei.project.controller;

import edu.yonsei.project.entity.ReviewEntity;
import edu.yonsei.project.entity.UserEntity;
import edu.yonsei.project.service.ReviewService;
import edu.yonsei.project.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    private final UserService userService;

    //마이페이지에서 작성한 전시회 후기 보여줌.
   @GetMapping("/home/mypage/reviews")
   public String showMypageReviews(HttpSession session, Model model) {
       String userId = (String) session.getAttribute("loginId");
       if (userId != null) {
           try {
               Optional<UserEntity> userOpt = userService.getUserByLoginId(userId);
               if (!userOpt.isPresent()) {
                   model.addAttribute("error", "사용자를 찾을 수 없습니다.");
                   return "errorPage"; // 사용자를 찾을 수 없는 경우 에러 페이지로 이동
               }

               UserEntity user = userOpt.get();
               model.addAttribute("nickname", user.getNickname()); // 닉네임을 모델에 추가

               List<ReviewEntity> reviews = reviewService.findByUserId(user.getLoginId());
               model.addAttribute("reviews", reviews);

               return "mypage_reviews";
           } catch (Exception e) {
               model.addAttribute("error", e.getMessage());
               return "errorPage";
           }
       } else {
           return "redirect:/home/login"; // 세션에 loginId가 없다면 로그인 페이지로 리다이렉트
       }
   }

    //리뷰 수정 페이지
    @GetMapping("/review/edit/{id}")
    public String editReview(@PathVariable("id") Long id, Model model) {
        ReviewEntity review = reviewService.getReviewById(id);
        model.addAttribute("review", review);
        return "mypage_review_edit"; // 리뷰 수정 페이지로 이동
    }

    //리뷰 수정 로직 처리.
    @PostMapping("/review/edit/{id}")
    public String updateReview(@PathVariable("id") Long id, @RequestParam("content") String content) {
        reviewService.updateReview(id, content);
        return "redirect:/home/mypage/reviews"; // 리뷰 목록 페이지로 리다이렉트
    }

    //리뷰 삭제 로직 처리.
    @PostMapping("/review/delete/{id}")
    public String deleteReview(@PathVariable("id") Long id) {
        reviewService.deleteReview(id);
        return "redirect:/home/mypage/reviews"; // 리뷰 목록 페이지로 리다이렉트
    }
}
