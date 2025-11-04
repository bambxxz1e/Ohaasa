package ohaasaSystem;

import java.sql.*;
import java.time.LocalDate;
import java.util.Properties;
import java.io.InputStream;
import model.User;
import model.DiaryEntry;

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
    public boolean validateLogin(String userId, String password) {
        String sql = "SELECT * FROM users WHERE user_id = ? AND password = ?";
        
        try (Connection conn = getConnection();
        	PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, userId);
            stmt.setString(2, password);
            
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
    
    // 일기 저장
    public boolean saveDiary(String userId, LocalDate date, String content, String imagePath,
            String zodiacSign, int rank, String advice, String action) {
		String sql = """
		INSERT INTO diaries (user_id, diary_date, content, image_path, zodiac_sign, horoscope_rank, horoscope_advice, horoscope_action)
		VALUES (?, ?, ?, ?, ?, ?, ?, ?)
		ON DUPLICATE KEY UPDATE
		content = VALUES(content),
		image_path = VALUES(image_path),
		zodiac_sign = VALUES(zodiac_sign),
		horoscope_rank = VALUES(horoscope_rank),
		horoscope_advice = VALUES(horoscope_advice),
		horoscope_action = VALUES(horoscope_action)
		""";
		
		try (Connection conn = getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql)) {
			pstmt.setString(1, userId);
			pstmt.setDate(2, Date.valueOf(date));
			pstmt.setString(3, content);
			pstmt.setString(4, imagePath);
			pstmt.setString(5, zodiacSign);
			pstmt.setInt(6, rank);
			pstmt.setString(7, advice);
			pstmt.setString(8, action);
			
			pstmt.executeUpdate();
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		return false;
		}
	}
    
    // 다이어리 불러오기
    public DiaryEntry getDiaryEntry(String userId, LocalDate date) {
        String sql = """
            SELECT content, image_path, zodiac_sign, horoscope_rank, horoscope_advice, horoscope_action
            FROM diaries WHERE user_id = ? AND diary_date = ?
            """;

        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, userId);
            pstmt.setDate(2, Date.valueOf(date));

            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                DiaryEntry entry = new DiaryEntry();
                entry.setText(rs.getString("content"));
                entry.setImagePath(rs.getString("image_path"));
                entry.setZodiacSign(rs.getString("zodiac_sign"));
                entry.setHoroscopeRank(rs.getInt("horoscope_rank"));
                entry.setAdvice(rs.getString("horoscope_advice"));
                entry.setAction(rs.getString("horoscope_action"));
                return entry;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // 특정 날짜 일기 조회
    public String getDiary(String userId, LocalDate date) {
        String sql = "SELECT content FROM diaries WHERE user_id=? AND diary_date=?";
        try (Connection conn = getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, userId);
            stmt.setDate(2, Date.valueOf(date));

            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString("content");
            }

        } catch (SQLException e) {
            System.err.println("일기 조회 실패: " + e.getMessage());
        }
        return null; // 일기가 없는 경우
    }

}