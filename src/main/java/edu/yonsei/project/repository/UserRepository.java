package edu.yonsei.project.repository;

import edu.yonsei.project.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
@Component
public interface UserRepository extends JpaRepository <UserEntity, Long> {
    Optional<UserEntity> findByLoginId(String loginId);
    boolean existsByLoginId(String loginId);
    boolean existsByNickname(String nickname);
    Optional<UserEntity> findByNameAndPhone(String name, String phone);
    Optional<UserEntity> findByNameAndLoginIdAndPhone(String name, String loginId, String phone);
}


