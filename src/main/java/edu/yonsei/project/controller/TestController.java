package edu.yonsei.project.controller;

import edu.yonsei.project.entity.CrawledData;
import edu.yonsei.project.entity.ReviewEntity;
import edu.yonsei.project.entity.UserEntity;
import edu.yonsei.project.repository.ReviewRepository;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import edu.yonsei.project.service.CrawlerService;

import java.util.List;
import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class TestController {

    @Autowired
    private CrawlerService crawlerService;

    private final UserService userService;

    @Autowired
    private ReviewService reviewService;

    //메인 페이지
    @GetMapping("/home")
    public String showHomePage(Model model) {
        List<CrawledData> activeExhibitions = crawlerService.getActiveExhibitions();
        model.addAttribute("exhibitions", activeExhibitions);
        return "main_page";
    }

    //로그인된 메인 페이지
    @GetMapping("/home_auth")
    public String showHomeAuthPage(Model model) {
        List<CrawledData> activeExhibitions = crawlerService.getActiveExhibitions();
        model.addAttribute("exhibitions", activeExhibitions);
        return "main_page_auth";
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
