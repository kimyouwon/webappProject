package edu.yonsei.project.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter
@Entity(name = "comments")
public class CommentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = true)
    private String userNickname; // 사용자 닉네임

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = false)
    private ReviewEntity review;

    @Column(nullable = false)
    private LocalDateTime createdAt; // 작성 날짜
}
