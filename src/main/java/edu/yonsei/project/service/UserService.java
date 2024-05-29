package edu.yonsei.project.service;

import edu.yonsei.project.dto.UserDto;
import edu.yonsei.project.entity.UserEntity;
import edu.yonsei.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Optional<UserEntity> getUserByLoginId(String loginId) {
        return userRepository.findByLoginId(loginId);
    }

    public boolean checkPassword(String inputPassword, String storedPassword) {
        // 여기서 저장된 비밀번호와 입력된 비밀번호를 비교하는 로직을 구현합니다.
        // 예를 들어, 해시된 비밀번호를 비교하는 경우 해시 비교 로직을 여기에 작성합니다.
        return inputPassword.equals(storedPassword);
    }

    @Transactional(readOnly = true)
    public String getNickname(String loginId) {
        return userRepository.findByLoginId(loginId)
                .map(UserEntity::getNickname)  // UserEntity에서 닉네임 추출
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Transactional
    public UserEntity postUser(UserDto userDto) {
        UserEntity entity = UserEntity.builder()
                .loginId(userDto.getLoginId())
                .name(userDto.getName())
                .nickname(userDto.getNickname())
                .password(userDto.getPassword())
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
        if (updatedUser.getPassword() != null) user.setPassword(updatedUser.getPassword());
        // 다른 필드도 마찬가지로 업데이트

        UserEntity saveEntity = userRepository.save(user);  // 변경된 엔티티 저장
        return true;
    }
    @Transactional
    public void updateUserPreference(String loginId, String preference) {
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

    public void updatePassword(String loginId, String newPassword) {
        UserEntity user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("User not found with login ID: " + loginId));
        user.setPassword(newPassword);
        userRepository.save(user);
    }
}
