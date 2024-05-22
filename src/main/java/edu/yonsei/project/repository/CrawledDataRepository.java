package edu.yonsei.project.repository;

import edu.yonsei.project.model.CrawledData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CrawledDataRepository extends JpaRepository<CrawledData, Long> {
    Optional<CrawledData> findByTitle(String title);
}
