package edu.yonsei.project.service;

import edu.yonsei.project.dto.UserDto;
import edu.yonsei.project.entity.UserEntity;
import edu.yonsei.project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Transactional
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserEntity getUserByLoginId(String loginId) throws Exception {
        return userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new Exception("User not found with loginId: " + loginId));
    }
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
                .gender(userDto.getGender())
                .birth(userDto.getBirth())
                .residence(userDto.getResidence())
                .interestedArea(userDto.getInterestedArea())
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
        if (updatedUser.getResidence() != null) user.setResidence(updatedUser.getResidence());
        if (updatedUser.getPassword() != null) user.setPassword(updatedUser.getPassword());
        // 다른 필드도 마찬가지로 업데이트

        UserEntity saveEntity = userRepository.save(user);  // 변경된 엔티티 저장
        return true;
    }

    @Transactional
    public void deleteUserByLoginId(String loginId) {
        UserEntity user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new RuntimeException("User not found with login ID: " + loginId));
        userRepository.delete(user);
    }
}
