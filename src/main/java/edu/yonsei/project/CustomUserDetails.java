package edu.yonsei.project;

/*import edu.yonsei.project.entity.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class CustomUserDetails implements UserDetails {
    private UserEntity user;

    public CustomUserDetails(UserEntity user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 간단한 예: 모든 사용자에게 "USER" 권한을 부여
        return Collections.singleton(new SimpleGrantedAuthority("USER"));
    }

    // 기타 UserDetails 메소드 구현
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getLoginId();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // 계정 만료 안됨
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // 계정 잠기지 않음
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // 자격 증명 만료 안됨
    }

    @Override
    public boolean isEnabled() {
        return true; // 계정 활성화 상태
    }
}
*/