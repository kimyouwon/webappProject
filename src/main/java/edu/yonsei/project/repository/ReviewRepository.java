package edu.yonsei.project.repository;

import edu.yonsei.project.entity.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewEntity, Long> {
    Optional<ReviewEntity> findFirstByExhibitionId(Long exhibitionId);
    List<ReviewEntity> findAllByExhibitionId(Long exhibitionId);
    List<ReviewEntity> findByExhibitionId(Long exhibitionId);
}
