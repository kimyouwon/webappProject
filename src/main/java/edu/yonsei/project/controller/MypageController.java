package edu.yonsei.project.controller;

import edu.yonsei.project.entity.UserEntity;
import edu.yonsei.project.service.ReviewService;
import edu.yonsei.project.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import edu.yonsei.project.service.CrawlerService;

import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class MypageController {

    @Autowired
    private CrawlerService crawlerService;

    private final UserService userService;

    @Autowired
    private ReviewService reviewService;

    //마이페이지 수정 전 디테일 페이지
    @GetMapping("home_auth/mypage/auth/edit_detail")
    public String showMypageEditDetail() {
        return "mypage_edit_detail";
    }

    // 마이페이지 수정 페이지
    @GetMapping("/home_auth/mypage/auth/edit")
    public String showMypageEditPage(HttpSession session, Model model) {
        String loginId = (String) session.getAttribute("loginId");
        if (loginId == null) {
            model.addAttribute("error", "로그인이 필요합니다.");
            return "redirect:/home/login"; // 로그인 안 되어 있으면 로그인 페이지로 리디렉션
        }
        try {
            Optional<UserEntity> userOpt = userService.getUserByLoginId(loginId);
            if (!userOpt.isPresent()) {
                model.addAttribute("error", "사용자를 찾을 수 없습니다.");
                return "errorPage"; // 사용자가 없을 경우 에러 페이지로 이동
            }
            UserEntity user = userOpt.get();
            model.addAttribute("user", user);
            return "mypage_edit";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "errorPage"; // 에러 발생 시 에러 페이지로 이동
        }
    }

    @PostMapping("/home_auth/mypage/auth/edit")
    public String updateUser(@ModelAttribute UserEntity user, HttpSession session, Model model) {
        String loginId = (String) session.getAttribute("loginId");

        /*if (loginId == null || !loginId.equals(user.getLoginId())) {
            model.addAttribute("error", "세션 정보가 유효하지 않습니다. 다시 로그인 해주세요.");
            return "redirect:/home/login";
        }*/ //이 코드 넣으니깐 계속 login 페이지로만 가짐
        try {
            userService.updateUser(loginId, user); // 사용자 정보 업데이트
            return "redirect:/home_auth/mypage"; // 업데이트 후 마이페이지로 리디렉션
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "errorPage"; // 에러 발생 시 에러 페이지로 이동
        }
    }

    //마이페이지
    @GetMapping("/home_auth/mypage")
    public String getUserInfo(HttpSession session, Model model) {
        String loginId = (String) session.getAttribute("loginId"); //세션에서 loginId를 기준으로 불러옴.

        if (loginId != null) {
            try {
                Optional<UserEntity> userOpt = userService.getUserByLoginId(loginId);
                if (!userOpt.isPresent()) {
                    model.addAttribute("error", "사용자를 찾을 수 없습니다.");
                    return "errorPage"; // 사용자를 찾을 수 없는 경우 에러 페이지로 이동
                }

                model.addAttribute("user", userOpt.get()); // 닉네임을 모델에 추가
                return "mypage";
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
                return "errorPage";
            }
        } else {
            return "redirect:/home/login"; // 세션에 loginId가 없다면 로그인 페이지로 리다이렉트
        }
    }
}