package edu.yonsei.project.controller;

import edu.yonsei.project.entity.CommentEntity;
import edu.yonsei.project.entity.ReviewEntity;
import edu.yonsei.project.entity.UserEntity;
import edu.yonsei.project.service.CommentService;
import edu.yonsei.project.service.UserService;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class CommentController {

    @Autowired
    private CommentService commentService;

    private final UserService userService;

    @GetMapping("/home/mypage/comments")
    public String getUserComments(Model model, HttpSession session) {
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

                List<CommentEntity> comments = commentService.getCommentsByUserId(user.getLoginId());
                model.addAttribute("comments", comments);
                return "mypage_comments";
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
                return "errorPage";
            }
        } else {
            return "redirect:/home/login"; // 세션에 loginId가 없다면 로그인 페이지로 리다이렉트
        }
    }
    //댓글 수정 페이지
    @GetMapping("/comment/edit/{id}")
    public String editComment(@PathVariable("id") Long id, Model model) {
        CommentEntity comment = commentService.getCommentById(id);
        model.addAttribute("comment", comment);
        return "mypage_comment_edit"; // 리뷰 수정 페이지로 이동
    }

    //댓글 수정 로직 처리.
    @PostMapping("/comment/edit/{id}")
    public String updateComment(@PathVariable("id") Long id, @RequestParam("content") String content) {
        commentService.updateComment(id, content);
        return "redirect:/home/mypage/comments"; // 댓글 목록 페이지로 리다이렉트
    }

    //댓글 삭제 로직 처리.
    @PostMapping("/comment/delete/{id}")
    public String deleteComment(@PathVariable("id") Long id) {
        commentService.deleteComment(id);
        return "redirect:/home/mypage/comments"; // 댓글 목록 페이지로 리다이렉트
    }
}