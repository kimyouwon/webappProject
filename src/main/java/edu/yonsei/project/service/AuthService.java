package edu.yonsei.project.service;

import edu.yonsei.project.entity.UserEntity;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class AuthService {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(UserService userService,  AuthenticationManager authenticationManager) {
        this.userService = userService;
        this.authenticationManager = authenticationManager;
    }

    public boolean authenticate(String loginId, String password) {
        // 사용자의 로그인 ID를 이용하여 Spring Security를 사용하여 사용자 인증을 수행
        Authentication authentication = new UsernamePasswordAuthenticationToken(loginId, password);
        try {
            // Spring Security의 인증 매니저를 사용하여 사용자를 인증
            Authentication result = authenticationManager.authenticate(authentication);
            // 인증이 성공하면 사용자 정보를 보안 컨텍스트에 저장
            SecurityContextHolder.getContext().setAuthentication(result);
            return true;
        } catch (AuthenticationException e) {
            // 인증이 실패하면 false를 반환
            return false;
        }
    }

    public boolean doesUserExist(String loginId) {
        // 사용자의 존재 여부를 Spring Security를 통해 확인
        return userService.existsByLoginId(loginId);
    }
    public boolean isAuthenticated(HttpSession session) {
        String loginId = (String) session.getAttribute("loginId");
        return loginId != null;
    }
}