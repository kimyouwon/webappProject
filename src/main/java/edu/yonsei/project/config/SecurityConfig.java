package edu.yonsei.project.config;

/*import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 인증 요구 사항 설정
                .authorizeRequests()
                .antMatchers("/home", "/login").permitAll()  // "/home" 및 "/login" 경로 접근 허용
                .anyRequest().authenticated()  // 그 외의 모든 요청은 인증 필요
                .and()
                // 로그인 설정
                .formLogin()
                .loginPage("/login")  // 사용자 정의 로그인 페이지 설정
                .defaultSuccessUrl("/home", true)  // 로그인 성공 후 리디렉션 페이지
                .permitAll()
                .and()
                // 로그아웃 설정
                .logout()
                .logoutSuccessUrl("/login")  // 로그아웃 성공 후 리디렉션 페이지
                .permitAll();

        return http.build();
    }
}
*/