package edu.yonsei.project.service;

import edu.yonsei.project.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import edu.yonsei.project.repository.UserRepository;
import edu.yonsei.project.entity.UserEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserSecurityService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String LoginId) throws UsernameNotFoundException {
        Optional<UserEntity> _userEntity = userRepository.findByLoginId(LoginId);
        if (_userEntity.isEmpty()) {
            throw new DataNotFoundException("사용자를 찾을수 없습니다 -> " + LoginId);
        }
        UserEntity userEntity = _userEntity.get();
        List<GrantedAuthority> authorities = new ArrayList<>();
        return new User(userEntity.getLoginId(), userEntity.getPassword(), authorities);
    }
}
