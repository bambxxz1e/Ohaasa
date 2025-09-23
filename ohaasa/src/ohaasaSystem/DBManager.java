package ohaasaSystem;

import java.sql.*;
import java.util.Properties;
import java.io.InputStream;
import model.User;

public class DBManager {
    private static String URL;
    private static String USER;
    private static String PASSWORD;

    // static 블록에서 properties 파일 읽기
    static {
        try (InputStream input = DBManager.class.getClassLoader().getResourceAsStream("db.properties")) {
            Properties prop = new Properties();
            if (input == null) {
                throw new RuntimeException("db.properties 파일을 찾을 수 없습니다.");
            }
            prop.load(input);

            URL = prop.getProperty("db.url");
            USER = prop.getProperty("db.user");
            PASSWORD = prop.getProperty("db.password");
        } catch (Exception e) {
            throw new RuntimeException("DB 설정 로딩 실패", e);
        }
    }

    // DB 연결 메소드
    private Connection getConnection() throws SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            return DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
            throw new SQLException("MySQL Driver not found", e);
        }
    }
    
    // 회원가입 처리
    public boolean registerUser(String userId, String password, String birthDate) {
        String sql = "INSERT INTO users (user_id, password, birth_date, zodiac_sign) VALUES (?, ?, ?, ?)";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            // User 객체 생성해서 별자리 계산
            User user = new User(userId, password, birthDate);
            
            stmt.setString(1, user.getUserId());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getBirthDate());
            stmt.setString(4, user.getZodiacSign());
            
            int result = stmt.executeUpdate();
            return result > 0;
            
        } catch (SQLException e) {
            System.err.println("회원가입 실패: " + e.getMessage());
            return false;
        }
    }
    
    // 로그인 검증
    public boolean validateLogin(String userId, String password, String birthDate) {
        String sql = "SELECT * FROM users WHERE user_id = ? AND password = ? AND birth_date = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, userId);
            stmt.setString(2, password);
            stmt.setString(3, birthDate);
            
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // 결과가 있으면 true, 없으면 false
            
        } catch (SQLException e) {
            System.err.println("로그인 검증 실패: " + e.getMessage());
            return false;
        }
    }
    
    // 사용자 정보 조회
    public User getUserInfo(String userId) {
        String sql = "SELECT * FROM users WHERE user_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                User user = new User(
                    rs.getString("user_id"),
                    rs.getString("password"),
                    rs.getString("birth_date")
                );
                return user;
            }
            
        } catch (SQLException e) {
            System.err.println("사용자 정보 조회 실패: " + e.getMessage());
        }
        
        return null;
    }
    
    // 아이디 중복 체크
    public boolean isUserIdExists(String userId) {
        String sql = "SELECT user_id FROM users WHERE user_id = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, userId);
            ResultSet rs = stmt.executeQuery();
            return rs.next(); // 결과가 있으면 true (중복), 없으면 false
            
        } catch (SQLException e) {
            System.err.println("아이디 중복 체크 실패: " + e.getMessage());
            return false;
        }
    }
}