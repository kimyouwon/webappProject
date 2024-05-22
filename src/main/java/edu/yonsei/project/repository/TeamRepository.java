package edu.yonsei.project.repository;

import edu.yonsei.project.entity.TeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository <TeamEntity, Long> {

}
