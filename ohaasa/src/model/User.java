package model;

public class User {
    private String userId;
    private String password;
    private String birthDate; // YYMMDD 형식
    private String zodiacSign;
    
    // 매개변수 생성자
    public User(String userId, String password, String birthDate) {
        this.userId = userId;
        this.password = password;
        this.birthDate = birthDate;
        this.zodiacSign = calculateZodiacSign(birthDate);
    }
    
    // Getter와 Setter
    public String getUserId() {
        return userId;
    }
    
    public void setUserId(String userId) {
        this.userId = userId;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getBirthDate() {
        return birthDate;
    }
    
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
        this.zodiacSign = calculateZodiacSign(birthDate);
    }
    
    public String getZodiacSign() {
        return zodiacSign;
    }
    
    public void setZodiacSign(String zodiacSign) {
        this.zodiacSign = zodiacSign;
    }
    
    // 생년월일로 별자리 계산하는 메소드
    private String calculateZodiacSign(String birthDate) {
        if (birthDate == null || birthDate.length() != 6) {
            return "알 수 없음";
        }
        
        try {
            // YYMMDD를 LocalDate로 변환
            int year = Integer.parseInt(birthDate.substring(0, 2));
            int month = Integer.parseInt(birthDate.substring(2, 4));
            int day = Integer.parseInt(birthDate.substring(4, 6));
            
            // 2000년대로 가정 (필요에 따라 수정)
            if (year >= 0 && year <= 30) {
                year += 2000;
            } else {
                year += 1900;
            }
            
            // 별자리 계산
            if ((month == 3 && day >= 21) || (month == 4 && day <= 19)) {
                return "양자리";
            } else if ((month == 4 && day >= 20) || (month == 5 && day <= 20)) {
                return "황소자리";
            } else if ((month == 5 && day >= 21) || (month == 6 && day <= 20)) {
                return "쌍둥이자리";
            } else if ((month == 6 && day >= 21) || (month == 7 && day <= 22)) {
                return "게자리";
            } else if ((month == 7 && day >= 23) || (month == 8 && day <= 22)) {
                return "사자자리";
            } else if ((month == 8 && day >= 23) || (month == 9 && day <= 22)) {
                return "처녀자리";
            } else if ((month == 9 && day >= 23) || (month == 10 && day <= 22)) {
                return "천칭자리";
            } else if ((month == 10 && day >= 23) || (month == 11 && day <= 21)) {
                return "전갈자리";
            } else if ((month == 11 && day >= 22) || (month == 12 && day <= 21)) {
                return "궁수자리";
            } else if ((month == 12 && day >= 22) || (month == 1 && day <= 19)) {
                return "염소자리";
            } else if ((month == 1 && day >= 20) || (month == 2 && day <= 18)) {
                return "물병자리";
            } else if ((month == 2 && day >= 19) || (month == 3 && day <= 20)) {
                return "물고기자리";
            }
            
        } catch (NumberFormatException e) {
            return "알 수 없음";
        }
        
        return "알 수 없음";
    }
    
    @Override
    public String toString() {
        return "User{" +
                "userId='" + userId + '\'' +
                ", password='" + password + '\'' +
                ", birthDate='" + birthDate + '\'' +
                ", zodiacSign='" + zodiacSign + '\'' +
                '}';
    }
}