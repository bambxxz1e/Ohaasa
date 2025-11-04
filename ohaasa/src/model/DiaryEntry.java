package model;

public class DiaryEntry {
    private String text;
    private String imagePath;
    private String zodiacSign;
    private int horoscopeRank;
    private String advice;
    private String action;

    public String getText() { return text; }
    public void setText(String text) { this.text = text; }

    public String getImagePath() { return imagePath; }
    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    public String getZodiacSign() { return zodiacSign; }
    public void setZodiacSign(String zodiacSign) { this.zodiacSign = zodiacSign; }

    public int getHoroscopeRank() { return horoscopeRank; }
    public void setHoroscopeRank(int horoscopeRank) { this.horoscopeRank = horoscopeRank; }

    public String getAdvice() { return advice; }
    public void setAdvice(String advice) { this.advice = advice; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }
}
