package edu.yonsei.project.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        // 로그인 성공 후 세션에 loginId 설정
        String loginId = authentication.getName(); // 기본적으로 username을 반환
        request.getSession().setAttribute("loginId", loginId);
        response.sendRedirect("/home"); // 기본 성공 URL로 리디렉트
    }
}
