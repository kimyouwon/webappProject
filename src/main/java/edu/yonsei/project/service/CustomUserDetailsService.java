package edu.yonsei.project.service;

import edu.yonsei.project.dto.UserDto;
import edu.yonsei.project.entity.UserEntity;
import edu.yonsei.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String loginId) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with loginId: " + loginId));
        return new CustomUserDetails(userEntity);
    }
}

