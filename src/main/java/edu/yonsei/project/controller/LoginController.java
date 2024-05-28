package edu.yonsei.project.controller;

import edu.yonsei.project.service.AuthService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class LoginController {
    private final AuthService authService;

    @Autowired
    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping("/home/login")
    public String showLoginPage(Model model, @RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "login_page"; // 로그인 페이지 반환
    }

    @PostMapping("/home/login")
    public String handleLogin(@RequestParam("loginId") String loginId,
                              @RequestParam("password") String password,
                              HttpSession session,
                              Model model) {
        if (!authService.doesUserExist(loginId)) {
            model.addAttribute("error", "존재하지 않는 아이디입니다");
            return "login_page";
        }

        if (authService.authenticate(loginId, password)) {
            session.setAttribute("loginId", loginId);
            return "redirect:/home_auth";
        } else {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다");
            return "login_page";
        }
    }

    // 마이페이지 수정하기 위한 인증페이지
    @GetMapping("/home_auth/mypage/auth")
    public String showMypageAuth(Model model, @RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "mypage_auth";
    }

    @PostMapping("/home_auth/mypage/auth/verify")
    public String verifyPassword(@RequestParam("password") String password, HttpSession session, Model model) {
        String loginId = (String) session.getAttribute("loginId");
        if (loginId == null) {
            return "redirect:/home/login"; // 세션에 loginId가 없을 경우 로그인 페이지로 리다이렉션
        }

        // AuthService를 사용하여 비밀번호 검증
        boolean isValid = authService.authenticate(loginId, password);
        if (isValid) {
            return "redirect:/home_auth/mypage/auth/edit"; // 비밀번호가 맞을 경우, 수정 페이지로 리다이렉션
        } else {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다");
            return "mypage_auth"; // 비밀번호가 틀릴 경우, 다시 인증 페이지로 리다이렉션
        }
    }
}
