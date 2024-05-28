package edu.yonsei.project.repository;

import edu.yonsei.project.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findByExhibitionId(Long exhibitionId);
    List<ReviewEntity> findByUserId(String userId);
}
