package edu.yonsei.project.dto;

import edu.yonsei.project.entity.CommentEntity;
import lombok.*;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter
public class ReviewDto {
    private Long id;        //고유 ID

    private String content; // 후기 내용
    private int rating;     // 평점
    private String userId;  // 사용자 ID
    private String userNickname; //사용자 닉네임
    private Long exhibitionId;    // 전시회 ID
    private String exhibitionName;    // 전시회 이름

    private List<CommentEntity> comments; // 댓글 리스트
    private long likeCount;            // 좋아요 개수
}
