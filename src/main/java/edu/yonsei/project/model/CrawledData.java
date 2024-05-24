package edu.yonsei.project.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class CrawledData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title; // 전시회 제목
    private String date; // 전시회 기간
    private String time; // 관람시간
    private String rest; // 휴관일
    private String genre; // 장르
    private String place; // 장소
    private String contact; // 연락처
    private String url; // 전시회 홈페이지
    private String image; // 이미지주소 (표 말고 왼쪽에 들어갈거)

    @Lob
    @Column(columnDefinition = "TEXT")
    private String fee; // 관람료

    @Lob
    @Column(columnDefinition = "TEXT")
    private String text; // 본문
}
