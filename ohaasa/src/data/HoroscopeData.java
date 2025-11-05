package data;

import model.Horoscope;
import ohaasaSystem.DBManager; // 이미 연결 메소드 있음
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class HoroscopeData {

    public static List<Horoscope> getTodayHoroscopes() {
        List<Horoscope> list = new ArrayList<>();
        LocalDate today = LocalDate.now();
        String sql = "SELECT `rank`, zodiacSign, advice FROM horoscope WHERE date = ? ORDER BY `rank` ASC";

        try (Connection conn = new DBManager().getDBConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setDate(1, Date.valueOf(today));
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                int rank = rs.getInt("rank");
                String zodiacSign = rs.getString("zodiacSign");
                String advice = rs.getString("advice");

                list.add(new Horoscope(rank, zodiacSign, advice));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }
}
