package edu.yonsei.project.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @Column(nullable = false)
    private String userNickname; // 사용자 ID

    @Column(nullable = false)
    private Long exhibitionId; // 사용자 ID

}
