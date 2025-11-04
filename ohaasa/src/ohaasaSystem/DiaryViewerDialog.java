package ohaasaSystem;

import java.awt.*;
import java.time.LocalDate;
import javax.swing.*;
import model.User;
import model.DiaryEntry;

public class DiaryViewerDialog extends JDialog {

    private DBManager dbManager;

    public DiaryViewerDialog(JFrame parent, LocalDate date, User user, DBManager dbManager) {
        super(parent, date + " 일기 보기", true);
        this.dbManager = dbManager;

        setSize(550, 600);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout(10, 10));
        getContentPane().setBackground(Color.WHITE);

        DiaryEntry diary = dbManager.getDiaryEntry(user.getUserId(), date);

        // 상단 날짜
        JLabel dateLabel = new JLabel(date.toString(), SwingConstants.CENTER);
        dateLabel.setFont(new Font("Pretendard", Font.BOLD, 18));
        dateLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        // 운세 & 별자리
        JTextArea horoscopeArea = new JTextArea();
        horoscopeArea.setEditable(false);
        horoscopeArea.setLineWrap(true);
        horoscopeArea.setWrapStyleWord(true);
        horoscopeArea.setFont(new Font("Pretendard", Font.PLAIN, 13));
        horoscopeArea.setBackground(new Color(245, 250, 255));
        horoscopeArea.setBorder(BorderFactory.createTitledBorder("오늘의 운세"));

        if (diary != null && diary.getZodiacSign() != null) {
            horoscopeArea.setText(
                    String.format("%d위 %s\n\n[조언]\n%s\n\n[행운의 행동]\n%s",
                    		diary.getHoroscopeRank(), diary.getZodiacSign(),
                            diary.getAdvice(), diary.getAction()));
        } else {
            horoscopeArea.setText("운세 정보가 없습니다.");
        }

        // 이미지
        JLabel imageLabel = new JLabel("", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(200, 200));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        if (diary != null && diary.getImagePath() != null) {
            ImageIcon icon = new ImageIcon(
                    new ImageIcon(diary.getImagePath()).getImage()
                            .getScaledInstance(200, 200, Image.SCALE_SMOOTH));
            imageLabel.setIcon(icon);
        } else {
            imageLabel.setText("첨부된 사진이 없습니다.");
        }

        // 일기 내용
        JTextArea diaryArea = new JTextArea();
        diaryArea.setEditable(false);
        diaryArea.setLineWrap(true);
        diaryArea.setWrapStyleWord(true);
        diaryArea.setFont(new Font("Pretendard", Font.PLAIN, 14));
        diaryArea.setText(diary != null ? diary.getText() : "작성된 일기가 없습니다.");

        JScrollPane diaryScroll = new JScrollPane(diaryArea);
        diaryScroll.setBorder(BorderFactory.createTitledBorder("일기 내용"));

        // 배치
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);
        topPanel.add(dateLabel, BorderLayout.NORTH);
        topPanel.add(horoscopeArea, BorderLayout.CENTER);

        JPanel centerPanel = new JPanel(new BorderLayout(10, 10));
        centerPanel.setBackground(Color.WHITE);
        centerPanel.add(diaryScroll, BorderLayout.CENTER);
        centerPanel.add(imageLabel, BorderLayout.EAST);

        add(topPanel, BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }
}
