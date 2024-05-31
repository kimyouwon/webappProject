package edu.yonsei.project.controller;

import edu.yonsei.project.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class LogoutController {
    private final UserService userService;


    //로그아웃 페이지
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/home/login?logout=true";
    }

    //회원 탈퇴 페이지
    @GetMapping("/home/mypage/delete")
    public String showDelete() {
        return "delete";
    }

    @PostMapping("/home/mypage/delete")
    public String deleteUser(HttpSession session, RedirectAttributes redirectAttributes) {
        String loginId = (String) session.getAttribute("loginId"); //세션에서 loginId를 기준으로 불러옴.
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
