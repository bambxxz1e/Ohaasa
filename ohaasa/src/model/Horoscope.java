package model;

public class Horoscope {
	private int rank;
	private String zodiacSign;
	private String advice;
	
	public Horoscope(int rank, String zodiacSign, String advice) {
        this.rank = rank;
        this.zodiacSign = zodiacSign;
        this.advice = advice;
    }

    // Getter & Setter
    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public String getZodiacSign() {
        return zodiacSign;
    }

    public void setZodiacSign(String zodiacSign) {
        this.zodiacSign = zodiacSign;
    }

    public String getAdvice() {
        return advice;
    }

    public void setAdvice(String advice) {
        this.advice = advice;
    }

    @Override
    public String toString() {
        return rank + "위 " + zodiacSign + " - " + advice;
    }
}
