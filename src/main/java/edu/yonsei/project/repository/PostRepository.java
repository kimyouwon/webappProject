package edu.yonsei.project.repository;


import edu.yonsei.project.dto.PostDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<PostDto, Long> {
}