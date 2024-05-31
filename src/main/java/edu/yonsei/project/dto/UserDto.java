package edu.yonsei.project.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.*;
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString

public class UserDto {
    @NotEmpty(message = "이름은 비워둘 수 없습니다.")
    private String name; //사용자의 본명(이름)

    @NotEmpty(message = "닉네임은 필수입니다.")
    private String nickname; //사용자의 닉네임

    @NotEmpty(message = "로그인 ID는 필수입니다.")
    private String loginId; //사용자의 아이디

    @NotEmpty(message = "비밀번호는 필수입니다.")
    @Size(min = 8, message = "비밀번호는 최소 8자 이상이어야 합니다.")
    private String password; //사용자의 패스워드

    @Email(message = "유효한 이메일 주소를 입력해야 합니다.")
    private String email; //사용자의 이메일

    @NotEmpty(message = "비밀번호는 필수입니다.")
    private String phone; //사용자의 전화번호

    private Integer age;  // Integer 객체 사용

    private Integer preference; //전시회 취향 테스트 결과


}
