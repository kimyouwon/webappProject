package edu.yonsei.project.security;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
                                        AuthenticationException exception) throws IOException, ServletException {
        String errorMessage = "Invalid username or password";
        if (exception.getMessage().contains("User not found")) {
            errorMessage = "User not found with provided loginId";
        } else if (exception.getMessage().contains("Invalid username or password")) {
            errorMessage = "Invalid username or password";
        }

        response.sendRedirect("/home/login?error=true&message=" + errorMessage);
    }
}

