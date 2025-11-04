package ohaasaSystem;

import model.Horoscope;
import model.User;
import data.HoroscopeData;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.time.LocalDate;
import java.util.Optional;

public class DiaryDialog extends JDialog {

    private JTextArea diaryArea;
    private JLabel imageLabel;
    private File selectedImage;
    private LocalDate date;
    private User currentUser;
    private DBManager dbManager;

    public DiaryDialog(JFrame parent, LocalDate date, User user, DBManager dbManager) {
        super(parent, "오늘의 일기", true);
        this.date = date;
        this.currentUser = user;
        this.dbManager = dbManager;
        
        // 메세지창 폰트
        UIManager.put("OptionPane.messageFont", new Font("Pretendard", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Pretendard", Font.BOLD, 12));

        setSize(500, 550);
        setLocationRelativeTo(parent);
        setLayout(new BorderLayout());
        getContentPane().setBackground(Color.WHITE);

        // 상단 - 날짜 + 별자리 + 운세
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setBackground(Color.WHITE);

        JLabel dateLabel = new JLabel(date.getYear() + "년 " 
                + date.getMonthValue() + "월 " + date.getDayOfMonth() + "일", SwingConstants.CENTER);
        dateLabel.setFont(new Font("Pretendard", Font.BOLD, 18));
        dateLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 5, 0));

        // 사용자 별자리 운세 불러오기
        String zodiac = currentUser.getZodiacSign();
        Optional<Horoscope> opt = HoroscopeData.getDummyData().stream()
                .filter(h -> h.getZodiacSign().equals(zodiac))
                .findFirst();

        String horoscopeText = "";
        if (opt.isPresent()) {
            Horoscope h = opt.get();
            horoscopeText = String.format("%d위 %s\n\n[조언]\n%s\n\n[행운의 행동]\n%s",
            		h.getRank(), h.getZodiacSign(), h.getAdvice(), h.getAction());
        }

        JTextArea horoscopeArea = new JTextArea(horoscopeText);
        horoscopeArea.setEditable(false);
        horoscopeArea.setLineWrap(true);
        horoscopeArea.setWrapStyleWord(true);
        horoscopeArea.setFont(new Font("Pretendard", Font.PLAIN, 14));
        horoscopeArea.setBackground(new Color(240, 248, 255)); // 연한 하늘색 배경
        horoscopeArea.setBorder(BorderFactory.createEmptyBorder(10, 15, 10, 15));

        topPanel.add(dateLabel, BorderLayout.NORTH);
        topPanel.add(horoscopeArea, BorderLayout.CENTER);

        // 중앙 - 일기 작성 영역
        diaryArea = new JTextArea();
        diaryArea.setLineWrap(true);
        diaryArea.setWrapStyleWord(true);
        diaryArea.setFont(new Font("Pretendard", Font.PLAIN, 14));
        diaryArea.setBorder(BorderFactory.createTitledBorder("오늘의 일기"));

        // 사진 미리보기 + 버튼
        JPanel imagePanel = new JPanel(new BorderLayout());
        imagePanel.setBackground(Color.WHITE);

        imageLabel = new JLabel("사진이 선택되지 않았습니다.", SwingConstants.CENTER);
        imageLabel.setPreferredSize(new Dimension(150, 150));
        imageLabel.setFont(new Font("Pretendard", Font.PLAIN, 14));
        imageLabel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        JButton imageButton = new JButton("사진 첨부");
        imageButton.setBackground(new Color(170, 210, 250));
        imageButton.setFont(new Font("Pretendard", Font.PLAIN, 14));
        imageButton.addActionListener(e -> chooseImage());

        imagePanel.add(imageLabel, BorderLayout.CENTER);
        imagePanel.add(imageButton, BorderLayout.SOUTH);

        // 하단 - 등록 버튼
        JButton saveButton = new JButton("등록");
        saveButton.setFont(new Font("Pretendard", Font.BOLD, 14));
        saveButton.setBackground(new Color(170, 210, 250));
        saveButton.addActionListener(e -> saveDiary());

        JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        southPanel.setBackground(Color.WHITE);
        southPanel.add(saveButton);

        // 메인 배치
        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(diaryArea), BorderLayout.CENTER);
        add(imagePanel, BorderLayout.EAST);
        add(southPanel, BorderLayout.SOUTH);
    }

    private void chooseImage() {
        JFileChooser chooser = new JFileChooser();
        int result = chooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            selectedImage = chooser.getSelectedFile();
            ImageIcon icon = new ImageIcon(
                    new ImageIcon(selectedImage.getAbsolutePath())
                            .getImage()
                            .getScaledInstance(150, 150, Image.SCALE_SMOOTH));
            imageLabel.setIcon(icon);
            imageLabel.setText("");
        }
    }

    private void saveDiary() {
        String text = diaryArea.getText().trim();
        if (text.isEmpty()) {
            JOptionPane.showMessageDialog(this, "일기 내용을 입력해주세요!", "오류", JOptionPane.WARNING_MESSAGE);
            return;
        }

        // 이미지 경로 처리
        String imagePath = selectedImage != null ? selectedImage.getAbsolutePath() : null;

        // 별자리 + 운세 정보
        String zodiac = currentUser.getZodiacSign();
        Horoscope h = HoroscopeData.getDummyData().stream()
                     .filter(hor -> hor.getZodiacSign().equals(zodiac))
                     .findFirst()
                     .orElse(null);

        int rank = h != null ? h.getRank() : 0;
        String advice = h != null ? h.getAdvice() : "";
        String action = h != null ? h.getAction() : "";

        // DB 저장 (확장된 saveDiary 메소드)
        boolean success = dbManager.saveDiary(
            currentUser.getUserId(),
            date,
            text,
            imagePath,
            zodiac,
            rank,
            advice,
            action
        );

        if (success) {
            JOptionPane.showMessageDialog(this, "일기가 등록되었습니다!", "저장 완료", JOptionPane.INFORMATION_MESSAGE);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "일기 저장에 실패했습니다.", "오류", JOptionPane.ERROR_MESSAGE);
        }
    }
}
