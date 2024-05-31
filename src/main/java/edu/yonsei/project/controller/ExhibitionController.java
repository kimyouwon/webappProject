package edu.yonsei.project.controller;

import edu.yonsei.project.dto.ReviewDto;
import edu.yonsei.project.dto.UserDto;
import edu.yonsei.project.entity.CommentEntity;
import edu.yonsei.project.entity.CrawledData;
import edu.yonsei.project.entity.ReviewEntity;
import edu.yonsei.project.entity.UserEntity;
import edu.yonsei.project.repository.CrawledDataRepository;
import edu.yonsei.project.repository.ReviewRepository;
import edu.yonsei.project.service.CommentService;
import edu.yonsei.project.service.LikeService;
import edu.yonsei.project.service.ReviewService;
import edu.yonsei.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpSession;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
public class ExhibitionController {

    @Autowired
    private CrawledDataRepository crawledDataRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CommentService commentService;

    @Autowired
    private LikeService likeService;

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @GetMapping("/exhibition/recommend")
    public String getExhibitionsByUserPreference(HttpSession session, Model model) {
        String loginId = (String) session.getAttribute("loginId");
        if (loginId != null) {
            Optional<UserEntity> userOpt = userService.getUserByLoginId(loginId);
            if (userOpt.isPresent()) {
                UserEntity user = userOpt.get();
                Integer preference = user.getPreference();
                if (preference == null){
                    return "redirect:/home/test";
                }
                return "redirect:/exh_keyword/" + preference;
            }
        }
        return "redirect:/home/login";
    }

    @GetMapping("/exhibition/{id}")
    public String getExhibitionDetail(@PathVariable("id") Long id, Model model) {
        try {
            Optional<CrawledData> exhibition = crawledDataRepository.findById(id);
            if (exhibition.isPresent()) {
                model.addAttribute("exhibition", exhibition.get());

                // 리뷰 데이터를 가져와 모델에 추가
                List<ReviewEntity> reviewEntities = reviewRepository.findByExhibitionId(id);
                List<ReviewDto> reviews = reviewEntities.stream().map(review -> {
                    ReviewDto reviewDto = ReviewDto.builder()
                            .id(review.getId())
                            .content(review.getContent())
                            .rating(review.getRating())
                            .userId(review.getUserId())
                            .exhibitionId(review.getExhibitionId())
                            .createdAt(review.getCreatedAt()) // 작성 시간 설정
                            .userNickname(review.getUserNickname()) // 사용자 닉네임 설정
                            .build();

                    // 댓글 엔티티를 가져옴
                    List<CommentEntity> comments = commentService.getCommentsByReview(review);
                    reviewDto.setComments(comments);

                    // 좋아요 개수 설정
                    reviewDto.setLikeCount(likeService.getLikeCountByReview(review));

                    return reviewDto;
                }).collect(Collectors.toList());

                model.addAttribute("reviews", reviews);

                return "exhibition_De_page";
            } else {
                model.addAttribute("errorMessage", "Exhibition not found");
                return "error";  // 전시회가 없을 경우 보여줄 오류 페이지
            }
        } catch (Exception e) {
            e.printStackTrace();  // 콘솔에 예외 메시지 출력
            model.addAttribute("errorMessage", "An error occurred while fetching the exhibition details");
            return "error";  // 예외가 발생했을 경우 보여줄 오류 페이지
        }
    }

    @PostMapping("/review/{reviewId}/comment")
    public String addComment(@PathVariable("reviewId") Long reviewId,
                             @RequestParam("content") String content,
                             HttpSession session,
                             Model model) {
        String userId = (String) session.getAttribute("loginId");
        if (userId == null) {
            return "redirect:/home/login";  // 로그인 페이지로 리다이렉트
        }

        Optional<ReviewEntity> reviewOpt = reviewRepository.findById(reviewId);
        if (reviewOpt.isPresent()) {
            ReviewEntity review = reviewOpt.get();
            CommentEntity comment = CommentEntity.builder()
                    .content(content)
                    .userId(userId)
                    .review(review)
                    .userNickname(userService.getNickname(userId))
                    .build();
            commentService.addComment(comment);
        }

        return "redirect:/exhibition/" + reviewOpt.get().getExhibitionId();
    }

    @PostMapping("/review/{reviewId}/like")
    public String addLike(@PathVariable("reviewId") Long reviewId, HttpSession session, Model model) {
        String userId = (String) session.getAttribute("loginId");
        if (userId == null) {
            return "redirect:/home/login";  // 로그인 페이지로 리다이렉트
        }

        Optional<ReviewEntity> reviewOpt = reviewRepository.findById(reviewId);
        if (reviewOpt.isPresent()) {
            ReviewEntity review = reviewOpt.get();
            boolean isLiked = likeService.isReviewLikedByUser(review, userId);
            if (!isLiked) {
                likeService.addLike(review, userId);
            }
        }

        return "redirect:/exhibition/" + reviewOpt.get().getExhibitionId();
    }

    @PostMapping("/exhibition/{id}/review")
    public String createReview(@PathVariable("id") Long id, @ModelAttribute ReviewEntity review, HttpSession session) {
        // 세션에서 사용자 정보 가져오기
        String userId = (String) session.getAttribute("loginId");
        if (userId == null) {
            return "redirect:/home/login";
        }

        // 전시회 Id, 이름을 불러오기 위한 부분
        Optional<CrawledData> exhibitionOpt = crawledDataRepository.findById(id);

        // 전시회 존재 여부 확인
        if (!exhibitionOpt.isPresent()) {
            return "redirect:/exhibition/" + id + "?error=ExhibitionNotFound";
        }

        CrawledData exhibition = exhibitionOpt.get();

        // 사용자 닉네임 가져오기
        String userNickname = userService.getNickname(userId);

        // 리뷰에 사용자 정보와 전시회 정보 설정
        review.setUserId(userId);
        review.setUserNickname(userNickname);
        review.setExhibitionId(exhibition.getId());
        review.setExhibitionName(exhibition.getTitle());

        reviewService.saveReview(review);
        return "redirect:/exhibition/" + id;
    }
}
