package ohaasaSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.*;
import model.User;

public class CalendarView extends JFrame {

	private User currentUser;
    private LocalDate currentDate = LocalDate.now();
    private JPanel calendarPanel;
    private JLabel monthLabel;
    DBManager dbManager = new DBManager();

    public CalendarView(User user) {
    	this.currentUser = user;
    	
        setTitle("오하아사 - 일기 캘린더");
        setSize(500, 450);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // 상단: 월 변경 버튼 + 월 표시
        JPanel topPanel = new JPanel(new BorderLayout());
        JButton prevButton = new JButton("◀");
        JButton nextButton = new JButton("▶");
        monthLabel = new JLabel("", SwingConstants.CENTER);
        monthLabel.setFont(new Font("Pretendard", Font.BOLD, 18));
        prevButton.setBackground(new Color(200, 230, 255));
        nextButton.setBackground(new Color(200, 230, 255));

        prevButton.addActionListener(e -> changeMonth(-1));
        nextButton.addActionListener(e -> changeMonth(1));

        topPanel.add(prevButton, BorderLayout.WEST);
        topPanel.add(monthLabel, BorderLayout.CENTER);
        topPanel.add(nextButton, BorderLayout.EAST);
        topPanel.setBackground(Color.WHITE);
        topPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));

        add(topPanel, BorderLayout.NORTH);

        // 중앙: 달력 패널
        calendarPanel = new JPanel(new GridLayout(0, 7, 5, 5));
        calendarPanel.setBackground(Color.WHITE);
        add(calendarPanel, BorderLayout.CENTER);
        calendarPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 15, 0));
        
        // 메세지 창 폰트 설정
        UIManager.put("OptionPane.messageFont", new Font("Pretendard", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Pretendard", Font.BOLD, 12));

        // 초기 달력 생성
        updateCalendar();

        setVisible(true);
    }

    private void updateCalendar() {
        calendarPanel.removeAll();

        // 월 라벨 설정
        monthLabel.setText(currentDate.getYear() + "년 " + currentDate.getMonthValue() + "월");

        // 요일 헤더
        String[] days = {"일", "월", "화", "수", "목", "금", "토"};
        for (String d : days) {
            JLabel lbl = new JLabel(d, SwingConstants.CENTER);
            lbl.setFont(new Font("Pretendard", Font.BOLD, 13));
            lbl.setForeground(d.equals("일") ? Color.RED : d.equals("토") ? Color.BLUE : Color.BLACK);
            calendarPanel.add(lbl);
        }

        // 해당 월의 시작 요일과 마지막 날짜 계산
        LocalDate firstDay = currentDate.withDayOfMonth(1);
        int startDay = firstDay.getDayOfWeek().getValue() % 7; // 일요일=0
        int lastDay = currentDate.lengthOfMonth();

        // 빈 칸 채우기 (시작 전 공백)
        for (int i = 0; i < startDay; i++) {
            calendarPanel.add(new JLabel(""));
        }

        // 날짜 버튼 생성
        for (int day = 1; day <= lastDay; day++) {
            LocalDate date = currentDate.withDayOfMonth(day);
            JButton dayBtn = new JButton(String.valueOf(day));
            dayBtn.setFocusPainted(false);
            dayBtn.setBackground(Color.WHITE);

            // 오늘 날짜 강조
            boolean isToday = date.equals(LocalDate.now());
            if (isToday) {
                dayBtn.setBorder(BorderFactory.createLineBorder(new Color(170, 210, 250), 2));
                dayBtn.setBackground(new Color(200, 230, 255));
            }
            
            // 일기 작성 여부 확인
            String diary = dbManager.getDiary(currentUser.getUserId(), date);
            if (diary != null) {
                String htmlText = "<html>" + day + "<font color='#54a6dd'> ●</font></html>";
                dayBtn.setText(htmlText);
            } else {
                // 일기 없는 날은 기본 숫자
                dayBtn.setText(String.valueOf(day));
            }

            // 버튼 클릭 시 팝업
            dayBtn.addActionListener(e -> {
            	if (date.equals(LocalDate.now())) {
                    String content = dbManager.getDiary(currentUser.getUserId(), date);
                    if (content != null) {
                        // 이미 작성된 오늘 일기가 있으면 읽기 전용으로 보기
                        new DiaryViewerDialog(this, date, currentUser, dbManager).setVisible(true);
                    } else {
                        // 아직 작성 안 한 오늘 일기면 작성 가능
                        new DiaryDialog(this, date, currentUser, dbManager).setVisible(true);
                    }
                } else {
                    // 과거 날짜는 항상 읽기 전용
                    new DiaryViewerDialog(this, date, currentUser, dbManager).setVisible(true);
                }
            });

            calendarPanel.add(dayBtn);
        }

        calendarPanel.revalidate();
        calendarPanel.repaint();
    }

    private void changeMonth(int offset) {
        currentDate = currentDate.plusMonths(offset);
        updateCalendar();
    }
}
