package edu.yonsei.project.repository;

import edu.yonsei.project.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository <UserEntity, Long> {
    Optional<UserEntity> findByLoginId(String loginId);
}


