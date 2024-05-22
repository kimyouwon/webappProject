package edu.yonsei.project.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
//로그인 인증 로직
@Service
public class AuthService {
    private DataSource dataSource;

    @Autowired
    public AuthService(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public boolean authenticate(String loginId, String password) {
        String storedPassword = null;  // 데이터베이스에서 조회된 비밀번호를 저장할 변수

        // 데이터베이스에서 사용자 정보 가져오기
        try (Connection connection = dataSource.getConnection();
             PreparedStatement stmt = connection.prepareStatement("SELECT password FROM users WHERE login_id = ?")) {
            stmt.setString(1, loginId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                storedPassword = rs.getString("password");  // 데이터베이스에서 비밀번호를 직접 가져옴
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // 사용자 정보가 없는 경우
        if (storedPassword == null) {
            System.out.println("User not found: " + loginId);
            return false;
        }

        // 입력된 비밀번호와 데이터베이스에 저장된 비밀번호 비교
        return storedPassword.equals(password);
    }


}
