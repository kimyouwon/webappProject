package edu.yonsei.project.dto;

import java.util.Date;

import jakarta.validation.constraints.NotEmpty;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class PostDto {
    private Long id; // 게시글의 ID (찡긋)

    @NotEmpty(message = "제목은 비워둘 수 없습니다.")
    private String title; // 게시글 제목 (웃음)

    @NotEmpty(message = "내용은 필수입니다.")
    private String content; // 게시글 내용 (박수)

    private Date createdDate; // 게시글 작성 날짜 (따봉)

    private String photo; // 게시글에 첨부할 사진 URL (헤헤)

    @NotEmpty(message = "사용자 ID는 필수입니다.")
    private String userId; // 게시글을 작성한 사용자의 ID (짱!)

    // 게시글과 연관된 UserDto 객체
    @ToString.Exclude
    private UserDto user; // 게시글을 작성한 사용자 정보 (히히)
}