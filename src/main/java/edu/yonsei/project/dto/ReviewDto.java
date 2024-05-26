package edu.yonsei.project.dto;

import lombok.*;

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
}
