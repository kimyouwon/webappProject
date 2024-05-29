package edu.yonsei.project.dto;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter
public class CommentDto {
    private Long id;

    private String content; // 댓글 내용
    private String userId;  // 댓글을 쓴 사용자의 Id
    private String userNickname; //사용자 닉네임
    private Long reviewId;  // 댓글이 달린 리뷰의 고유 Id
    private LocalDateTime createdAt; // 작성 날짜
}
