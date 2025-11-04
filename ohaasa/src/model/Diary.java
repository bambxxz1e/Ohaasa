package model;

import java.time.LocalDate;

public class Diary {
    private String userId;      // 외래키 User.userId
    private LocalDate date;     // 일기 작성 날짜
    private String content;     // 일기 내용
    private String imagePath;   // 첨부 이미지 경로 (선택)

    public Diary(String userId, LocalDate date, String content, String imagePath) {
        this.userId = userId;
        this.date = date;
        this.content = content;
        this.imagePath = imagePath;
    }

    // Getter & Setter
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }
}
