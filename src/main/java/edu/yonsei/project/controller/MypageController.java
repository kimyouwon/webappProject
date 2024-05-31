package edu.yonsei.project.controller;

import edu.yonsei.project.dto.UserDto;
import edu.yonsei.project.entity.UserEntity;
import edu.yonsei.project.service.AuthService;
import edu.yonsei.project.service.ReviewService;
import edu.yonsei.project.service.UserService;

import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;

//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import edu.yonsei.project.service.CrawlerService;

import java.util.Optional;


@Controller
@RequiredArgsConstructor
public class MypageController {

    private final AuthService authService;

    @Autowired
    private CrawlerService crawlerService;

    private final UserService userService;

    @Autowired
    private ReviewService reviewService;

    //마이페이지 수정 전 디테일 페이지
    @GetMapping("home/mypage/auth/edit_detail")
    public String showMypageEditDetail() {
        return "mypage_edit_detail";
    }

    // 마이페이지 수정 페이지
    @GetMapping("/home/mypage/auth/edit")
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

    @PostMapping("/home/mypage/auth/edit")
    public String updateUser(@ModelAttribute UserEntity user, HttpSession session, Model model) {
        String loginId = (String) session.getAttribute("loginId");
        try {
            userService.updateUser(loginId, user); // 사용자 정보 업데이트
            return "redirect:/home/mypage"; // 업데이트 후 마이페이지로 리디렉션
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "errorPage"; // 에러 발생 시 에러 페이지로 이동
        }
    }

    //마이페이지
    @GetMapping("/home/mypage")
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

    // 취향 테스트 결과 추가
    @PostMapping("/home/mypage/save-result")
    @ResponseBody
    public ResponseEntity<?> saveUserPreference(@RequestBody UserDto userDto, HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");

        if (loginId != null) {
            try {
                Optional<UserEntity> userOpt = userService.getUserByLoginId(loginId);
                if (!userOpt.isPresent()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("{\"message\": \"사용자를 찾을 수 없습니다.\"}");
                }

                userService.updateUserPreference(loginId, userDto.getPreference());
                return ResponseEntity.ok("{\"message\": \"결과가 WETE에 저장되었습니다!\"}");
            } catch (Exception e) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body("{\"message\": \"" + e.getMessage() + "\"}");
            }
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("{\"message\": \"먼저 WETE에 로그인해주세요.\"}");
        }
    }
    // 마이페이지 수정하기 위한 인증페이지
    @GetMapping("/home/mypage/auth")
    public String showMypageAuth(Model model, @RequestParam(value = "error", required = false) String error) {
        if (error != null) {
            model.addAttribute("error", error);
        }
        return "mypage_auth";
    }

    @PostMapping("/home/mypage/auth/verify")
    public String verifyPassword(@RequestParam("password") String password, HttpSession session, Model model) {
        String loginId = (String) session.getAttribute("loginId");
        if (loginId == null) {
            return "redirect:/home/login"; // 세션에 loginId가 없을 경우 로그인 페이지로 리다이렉션
        }

        // AuthService를 사용하여 비밀번호 검증
        boolean isValid = authService.authenticate(loginId, password);
        if (isValid) {
            return "redirect:/home/mypage/auth/edit"; // 비밀번호가 맞을 경우, 수정 페이지로 리다이렉션
        } else {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다");
            return "mypage_auth"; // 비밀번호가 틀릴 경우, 다시 인증 페이지로 리다이렉션
        }
    }
}