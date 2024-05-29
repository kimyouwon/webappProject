package edu.yonsei.project.controller;

import edu.yonsei.project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import jakarta.servlet.http.HttpSession;

@ControllerAdvice
public class GlobalControllerAdvice {

    @Autowired
    private UserService userService;

    @ModelAttribute("isLoggedIn")
    public boolean isLoggedIn(HttpSession session) {
        return session.getAttribute("loginId") != null;
    }

    @ModelAttribute("userNickname")
    public String userNickname(HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");
        return loginId != null ? userService.getNickname(loginId) : null;
    }
}
