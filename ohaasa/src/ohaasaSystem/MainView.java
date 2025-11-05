package ohaasaSystem;

import javax.swing.*;
import java.util.List;
import java.awt.*;
import model.Horoscope;
import model.User;
import data.HoroscopeData;
import java.util.Optional;
import java.util.Calendar;

public class MainView extends JFrame {

    private User currentUser;
    Calendar c = Calendar.getInstance();
    String today = c.get(Calendar.YEAR) + "년 " 
    			+ (c.get(Calendar.MONTH) + 1) + "월 " 
    			+ c.get(Calendar.DATE) + "일";

    public MainView(User user) {
        this.currentUser = user;

        setTitle("오하아사 - 오늘의 별자리 운세");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        initUI();

        setVisible(true);
    }

    private void initUI() {
        // 배경색과 기본 폰트
        getContentPane().setBackground(Color.WHITE);
        setLayout(new BorderLayout());

        // 제목 라벨
        JLabel titleLabel = new JLabel(today + " 오하아사", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Pretendard", Font.BOLD, 20));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        add(titleLabel, BorderLayout.NORTH);
        
        // 사용자 별자리 가져오기
        List<Horoscope> horoscopeList = new HoroscopeData().getTodayHoroscopes();

        // 사용자 별자리 가져오기
        String zodiac = currentUser.getZodiacSign();
        Optional<Horoscope> opt = horoscopeList.stream()
        	    .filter(h -> h.getZodiacSign().trim().equalsIgnoreCase(zodiac.trim()))
        	    .findFirst();
        
        // 중앙 패널
        JPanel centerPanel = new JPanel(new BorderLayout());
        centerPanel.setBackground(Color.WHITE);

        if (opt.isPresent()) {
        	Horoscope h = opt.get();

            JLabel zodiacLabel = new JLabel(h.getRank() + "위 " + h.getZodiacSign(), SwingConstants.CENTER);
            zodiacLabel.setFont(new Font("Pretendard", Font.BOLD, 22));
            zodiacLabel.setBorder(BorderFactory.createEmptyBorder(5, 0, 15, 0));

            JTextArea infoArea = new JTextArea();
            infoArea.setFont(new Font("Pretendard", Font.PLAIN, 15));
            infoArea.setEditable(false);
            infoArea.setLineWrap(true);
            infoArea.setWrapStyleWord(true);
            infoArea.setText("\n[조언 및 행운의 행동]\n\n" + h.getAdvice());

            infoArea.setFont(new Font("Pretendard", Font.PLAIN, 16));
            
            
            infoArea.setBackground(new Color(250, 250, 250));
            infoArea.setBorder(BorderFactory.createEmptyBorder(15, 20, 15, 20));

            centerPanel.add(zodiacLabel, BorderLayout.NORTH);
            centerPanel.add(new JScrollPane(infoArea), BorderLayout.CENTER);
        } else {
            JLabel noData = new JLabel("해당 별자리 정보를 찾을 수 없습니다.", SwingConstants.CENTER);
            noData.setFont(new Font("Pretendard", Font.PLAIN, 16));
            centerPanel.add(noData, BorderLayout.CENTER);
        }

        add(centerPanel, BorderLayout.CENTER);

     // 하단 안내 문구
        JLabel tipLabel = new JLabel("행복한 하루 보내세요!", SwingConstants.CENTER);
        tipLabel.setFont(new Font("Pretendard", Font.ITALIC, 13));
        tipLabel.setForeground(Color.GRAY);
        tipLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // 버튼 2개 생성
        JButton goRankButton = new JButton("전체 순위 보러가기");
        goRankButton.setFont(new Font("Pretendard", Font.BOLD, 14));
        goRankButton.setBackground(new Color(170, 210, 250));
        goRankButton.setFocusPainted(false);
        goRankButton.addActionListener(e -> {
        	new RankView(currentUser).setVisible(true);
            dispose();
        });

        JButton goCalendarButton = new JButton("캘린더 보기");
        goCalendarButton.setFont(new Font("Pretendard", Font.BOLD, 14));
        goCalendarButton.setBackground(new Color(170, 210, 250));
        goCalendarButton.setFocusPainted(false);
        goCalendarButton.addActionListener(e -> {
            new CalendarView(currentUser).setVisible(true);
            dispose();
        });

        // 버튼들을 담을 패널 (1행 2열로 반반 배치)
        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 10, 40));
        buttonPanel.add(goRankButton);
        buttonPanel.add(goCalendarButton);

        // 하단 전체 구성
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(Color.WHITE);
        southPanel.add(tipLabel, BorderLayout.NORTH);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);

        add(southPanel, BorderLayout.SOUTH);

    }
    
//    // 실행 테스트
//    public static void main(String[] args) {
//    	User u = new User("test", "123", "080322");
//        new MainView(u); // 첫 화면
//    }
}
