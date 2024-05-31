package edu.yonsei.project.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter
@Entity(name="reviews")
public class ReviewEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content; // 후기 내용

    @Column(nullable = false)
    private int rating; // 평점

    @Column(nullable = false)
    private String userId; // 사용자 ID

    @Column(nullable = true)
    private String userNickname; // 사용자 닉네임

    @Column(nullable = false)
    private Long exhibitionId; // 전시회 ID

    @Column(nullable = true)
    private String exhibitionName; // 전시회 이름

    @OneToMany(mappedBy = "review", cascade = CascadeType.REMOVE)
    private List<CommentEntity> comments; // 댓글 리스트

    private long likeCount; // 좋아요 개수

    @Column(nullable = false)
    private LocalDateTime createdAt; // 작성 날짜
}
