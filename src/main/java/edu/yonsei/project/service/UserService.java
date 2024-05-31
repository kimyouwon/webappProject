package edu.yonsei.project.service;

import edu.yonsei.project.dto.UserDto;
import edu.yonsei.project.entity.UserEntity;
import edu.yonsei.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<UserEntity> userOpt = userRepository.findByLoginId(username);
        if (!userOpt.isPresent()) {
            throw new UsernameNotFoundException("User not found with loginId: " + username);
        }
        UserEntity user = userOpt.get();
        return new org.springframework.security.core.userdetails.User(
                user.getLoginId(),
                user.getPassword(),
                Collections.emptyList()
        );
    }


    public Optional<UserEntity> getUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId);
    }

    public boolean checkPassword(String inputPassword, String storedPassword) {
        // 여기서 저장된 비밀번호와 입력된 비밀번호를 비교하는 로직을 구현합니다.
        // 예를 들어, 해시된 비밀번호를 비교하는 경우 해시 비교 로직을 여기에 작성합니다.
        return passwordEncoder.matches(inputPassword, storedPassword);
    }

    @Transactional(readOnly = true)
    public String getNickname(String loginId) {
        return userRepository.findByLoginId(loginId)
                .map(UserEntity::getNickname)  // UserEntity에서 닉네임 추출
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Transactional
    public UserEntity postUser(UserDto userDto) {
        String encodedPassword = passwordEncoder.encode(userDto.getPassword());
        UserEntity entity = UserEntity.builder()
                .loginId(userDto.getLoginId())
                .name(userDto.getName())
                .nickname(userDto.getNickname())
                .password(encodedPassword)
                .email(userDto.getEmail())
                .phone(userDto.getPhone())
                .age(userDto.getAge())
                .birth(userDto.getBirth())
                .preference(userDto.getPreference())
                .build();

        return userRepository.save(entity);
    }

    @Transactional
    public boolean updateUser(String loginId, UserEntity updatedUser) {
        UserEntity user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (updatedUser.getName() != null) user.setName(updatedUser.getName());
        if (updatedUser.getEmail() != null) user.setEmail(updatedUser.getEmail());
        if (updatedUser.getPhone() != null) user.setPhone(updatedUser.getPhone());
        if (updatedUser.getNickname() != null) user.setNickname(updatedUser.getNickname());
        if (updatedUser.getPassword() != null) {
            String encodedPassword = passwordEncoder.encode(updatedUser.getPassword());
            user.setPassword(encodedPassword);
        }

        UserEntity saveEntity = userRepository.save(user);  // 변경된 엔티티 저장
        return true;
    }
    @Transactional
    public void updateUserPreference(String loginId, int preference) {
        UserEntity user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("User not found with login ID: " + loginId));
        user.setPreference(preference);
        userRepository.save(user);
    }

    @Transactional
    public void deleteUserByLoginId(String loginId) {
        UserEntity user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("User not found with login ID: " + loginId));
        userRepository.delete(user);
    }

    public boolean existsByLoginId(String loginId) {
        return userRepository.existsByLoginId(loginId);
    }

    public boolean existsByNickname(String nickname) {
        return userRepository.existsByNickname(nickname);
    }

    public Optional<UserEntity> findByNameAndPhone(String name, String phone) {
        return userRepository.findByNameAndPhone(name, phone);
    }

    public Optional<UserEntity> findByNameAndLoginIdAndPhone(String name, String loginId, String phone) {
        return userRepository.findByNameAndLoginIdAndPhone(name, loginId, phone);
    }

    public void updatePassword(String loginId, String currentPassword, String newPassword) {
        UserEntity user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("로그인 ID에 해당하는 사용자를 찾을 수 없습니다."));

        if (!passwordEncoder.matches(currentPassword, user.getPassword())) {
            throw new RuntimeException("현재 비밀번호가 일치하지 않습니다.");
        }

        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);

        userRepository.save(user);
    }

    public void resetPassword(String loginId, String newPassword) {
        UserEntity user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("로그인 ID에 해당하는 사용자를 찾을 수 없습니다."));

        String encodedNewPassword = passwordEncoder.encode(newPassword);
        user.setPassword(encodedNewPassword);

        userRepository.save(user);
    }

}