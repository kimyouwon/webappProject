package edu.yonsei.project.service;

import edu.yonsei.project.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class AuthService {
    private static final Logger logger = Logger.getLogger(AuthService.class.getName());
    private final UserService userService;

    @Autowired
    public AuthService(UserService userService) {
        this.userService = userService;
    }

    public boolean authenticate(String loginId, String password) {
        Optional<UserEntity> userOpt = userService.getUserByLoginId(loginId);

        if (!userOpt.isPresent()) {
            logger.warning("User not found: " + loginId);
            return false;
        }

        UserEntity user = userOpt.get();
        boolean isAuthenticated = userService.checkPassword(password, user.getPassword());
        if (isAuthenticated) {
            logger.info("User authenticated: " + loginId);
        } else {
            logger.warning("Authentication failed for user: " + loginId);
        }
        return isAuthenticated;
    }
}
