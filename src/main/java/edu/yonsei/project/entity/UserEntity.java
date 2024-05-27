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
@Entity(name="users")

public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; //사용자의 아이디

    @Column(unique = true)
    private String loginId; //사용자의 아이디

    private String name; //사용자의 본명(이름)
    private String nickname; //사용자의 닉네임
    private String password; //사용자의 패스워드
    private String email; //사용자의 이메일
    private String phone; //사용자의 전화번호
    private Integer age; //사용자의 나이

    private java.util.Date birth; // 사용자의 생일

    private String preference; //전시회 취향 테스트 결과


}
