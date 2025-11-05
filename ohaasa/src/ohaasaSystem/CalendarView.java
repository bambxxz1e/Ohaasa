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
        setSize(500, 500);
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

        // 하단: 버튼 2개 구성
        JButton goMainButton = new JButton("나의 오하아사 확인하기");
        goMainButton.setFont(new Font("Pretendard", Font.BOLD, 14));
        goMainButton.setBackground(new Color(170, 210, 250));
        goMainButton.setFocusPainted(false);
        goMainButton.addActionListener(e -> {
            new MainView(currentUser).setVisible(true);
            dispose();
        });

        JButton goRankButton = new JButton("전체 순위 보러가기");
        goRankButton.setFont(new Font("Pretendard", Font.BOLD, 14));
        goRankButton.setBackground(new Color(170, 210, 250));
        goRankButton.setFocusPainted(false);
        goRankButton.addActionListener(e -> {
        	new MainView(currentUser).setVisible(true);
            dispose();
        });

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 40, 10, 40));
        buttonPanel.add(goMainButton);
        buttonPanel.add(goRankButton);

        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(Color.WHITE);
        southPanel.add(buttonPanel, BorderLayout.SOUTH);
        add(southPanel, BorderLayout.SOUTH);

        // 메시지 폰트 설정
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
        int startDay = firstDay.getDayOfWeek().getValue() % 7;
        int lastDay = currentDate.lengthOfMonth();

        // 빈 칸
        for (int i = 0; i < startDay; i++) {
            calendarPanel.add(new JLabel(""));
        }

        // 날짜 버튼 생성
        for (int day = 1; day <= lastDay; day++) {
            LocalDate date = currentDate.withDayOfMonth(day);
            JButton dayBtn = new JButton(String.valueOf(day));
            dayBtn.setFocusPainted(false);
            dayBtn.setBackground(Color.WHITE);

            boolean isToday = date.equals(LocalDate.now());
            if (isToday) {
                dayBtn.setBorder(BorderFactory.createLineBorder(new Color(170, 210, 250), 2));
                dayBtn.setBackground(new Color(200, 230, 255));
            }

            String diary = dbManager.getDiary(currentUser.getUserId(), date);
            if (diary != null) {
                String htmlText = "<html>" + day + "<font color='#54a6dd'> ●</font></html>";
                dayBtn.setText(htmlText);
            }

            dayBtn.addActionListener(e -> {
                if (date.equals(LocalDate.now())) {
                    String content = dbManager.getDiary(currentUser.getUserId(), date);
                    if (content != null) {
                        new DiaryViewerDialog(this, date, currentUser, dbManager).setVisible(true);
                    } else {
                        new DiaryDialog(this, date, currentUser, dbManager).setVisible(true);
                    }
                } else {
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
