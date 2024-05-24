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
    private Long id; //고유 ID

    private String content; // 후기 내용
    private int rating; // 평점
    private Long userId; // 사용자 ID
}
