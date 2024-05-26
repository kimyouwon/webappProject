package edu.yonsei.project.repository;

import edu.yonsei.project.entity.CrawledData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CrawledDataRepository extends JpaRepository<CrawledData, Long> {
    Optional<CrawledData> findByTitle(String title);
}
