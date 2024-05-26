package edu.yonsei.project.controller;

import edu.yonsei.project.entity.UserEntity;
import edu.yonsei.project.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;


@Controller
@RequiredArgsConstructor
public class TestController {

    private final UserService userService;

    //메인 페이지
    @GetMapping("/home")
    public String showHomePage() {
        return "main_page";
    }

    //로그인된 메인 페이지
    @GetMapping("/home_auth")
    public String showHomeAuthPage() {
        return "main_page_auth";
    }

    //취향 테스트
    @GetMapping({"/home/test", "/home_auth/test"})
    public String showTestPage() {
        return "index";
    }

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
            UserEntity user = userService.getUserByLoginId(loginId);
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

    //본인이 작성한 후기 모음집
    @GetMapping("/home_auth/mypage/reviews")
    public String showMypageReviews() {
        return "mypage_reviews";
    }

    //마이페이지
    @GetMapping("/home_auth/mypage")
    public String getUserInfo(HttpSession session, Model model) {
        String loginId = (String) session.getAttribute("loginId"); //세션에서 loginId를 기준으로 불러옴.

        if (loginId != null) {
            try {
                UserEntity user = userService.getUserByLoginId(loginId);
                model.addAttribute("nickname", user.getNickname());  // 닉네임을 모델에 추가
                model.addAttribute("email", user.getEmail());
                model.addAttribute("phone", user.getPhone());
                model.addAttribute("residence", user.getResidence());
                return "mypage";
            } catch (Exception e) {
                model.addAttribute("error", e.getMessage());
                return "errorPage";
            }
        } else {
            return "redirect:/home/login"; // 세션에 loginId가 없다면 로그인 페이지로 리다이렉트
        }
    }
    //회원 탈퇴 페이지
    @GetMapping("/home_auth/mypage/delete")
    public String showDelete() {
        return "delete";
    }
    @PostMapping("/home_auth/mypage/delete")
    public String deleteUser(HttpSession session, RedirectAttributes redirectAttributes) {
        String loginId = (String) session.getAttribute("loginId"); //세션에서 loginId를 기준으로 불러옴.
       /* if (userId == null) {
            redirectAttributes.addFlashAttribute("error", "로그인이 필요합니다.");
            return "redirect:/login";  // 로그인 페이지로 리다이렉션
        }*/
        try {
            userService.deleteUserByLoginId(loginId);
            session.invalidate();  // 탈퇴 후 세션 무효화
            redirectAttributes.addFlashAttribute("message", "회원 탈퇴가 완료되었습니다.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "회원 탈퇴 중 오류가 발생했습니다.");
            return "redirect:/error";  // 설정 페이지로 리다이렉션
        }

        return "redirect:/home";  // 홈 페이지 또는 로그인 페이지로 리다이렉션
    }

}
