package model;

public class Horoscope {
	private int rank;
	private String zodiacSign;
	private String advice;
	private String action;
	
	public Horoscope(int rank, String zodiacSign, String advice, String action) {
        this.rank = rank;
        this.zodiacSign = zodiacSign;
        this.advice = advice;
        this.action = action;
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

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    @Override
    public String toString() {
        return rank + "위 " + zodiacSign + " - " + advice + " (" + action + ")";
    }
}
