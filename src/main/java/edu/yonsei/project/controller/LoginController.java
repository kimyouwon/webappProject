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
            return "redirect:/home";
        } else {
            model.addAttribute("error", "비밀번호가 일치하지 않습니다");
            return "login_page";
        }
    }


}
