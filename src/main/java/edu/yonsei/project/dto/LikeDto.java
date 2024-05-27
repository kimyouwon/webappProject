package edu.yonsei.project.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Builder
@Getter
@Setter
public class LikeDto {
    private Long id;

    private String userId;  // 사용자 login Id
    private Long reviewId;  // 리뷰 고유 Id
}
