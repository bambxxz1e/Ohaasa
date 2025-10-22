package ohaasaSystem;

import data.HoroscopeData;
import model.Horoscope;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import java.util.Calendar;

public class RankView extends JFrame {

	private User currentUser;
    private JTable table;
    private List<Horoscope> horoscopeList;
    Calendar c = Calendar.getInstance();
    String today = c.get(Calendar.YEAR) + "년 " 
    			+ (c.get(Calendar.MONTH) + 1) + "월 " 
    			+ c.get(Calendar.DATE) + "일";

    public RankView(User user) {
    	this.currentUser = user;
    	UIManager.put("OptionPane.messageFont", new Font("Pretendard", Font.PLAIN, 14));
        UIManager.put("OptionPane.buttonFont", new Font("Pretendard", Font.BOLD, 12));
    	
        setTitle("오늘의 별자리 순위");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // 데이터 불러오기
        horoscopeList = HoroscopeData.getDummyData();

        // 테이블 모델 생성 (순위, 별자리만 표시)
        String[] columnNames = {"순위", "별자리"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);

        for (Horoscope h : horoscopeList) {
            tableModel.addRow(new Object[]{h.getRank(), h.getZodiacSign()});
        }

        // JTable 설정
        table = new JTable(tableModel);
        table.setRowHeight(30);
        table.setFont(new Font("Pretendard", Font.PLAIN, 13));
        table.getTableHeader().setFont(new Font("Pretendard", Font.BOLD, 14));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        // 가운데 정렬 렌더러 추가
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(SwingConstants.CENTER);

        // 순위, 별자리 열 모두 가운데 정렬
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        // 첫 번째 열(순위) 폭 살짝 좁히기
        table.getColumnModel().getColumn(0).setMaxWidth(70);

        // 테이블 클릭 이벤트 (팝업)
        table.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                int selectedRow = table.getSelectedRow();
                if (selectedRow >= 0) {
                    Horoscope selected = horoscopeList.get(selectedRow);
                    String message = 
                        selected.getRank() + "위 " + selected.getZodiacSign() + "\n\n" +
                        "[조언] " + selected.getAdvice() + "\n\n" +
                        "[행운의 행동] " + selected.getAction();
                    JOptionPane.showMessageDialog(
                        RankView.this,
                        message,
                        selected.getZodiacSign() + "의 운세",
                        JOptionPane.INFORMATION_MESSAGE
                    );
                }
            }
        });

        // 스크롤 패널에 테이블 추가
        JScrollPane scrollPane = new JScrollPane(table);

        // 제목 라벨
        JLabel titleLabel = new JLabel(today + " 오하아사", SwingConstants.CENTER);
        titleLabel.setFont(new Font("Pretendard", Font.BOLD, 18));
        titleLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

        // 하단 안내문
        JLabel tipLabel = new JLabel("별자리를 클릭하면 오늘의 운세를 볼 수 있어요!", SwingConstants.CENTER);
        tipLabel.setFont(new Font("Pretendard", Font.ITALIC, 12));
        tipLabel.setForeground(Color.GRAY);
        tipLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        
        JButton goMainButton = new JButton("나의 오하아사 확인하기");
        goMainButton.setFont(new Font("Pretendard", Font.BOLD, 14));
        goMainButton.setBackground(new Color(200, 230, 255));
        goMainButton.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));
        goMainButton.setFocusPainted(false);
        goMainButton.addActionListener(e -> {
            new MainView(currentUser).setVisible(true);
            dispose();
        });
        
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.setBackground(Color.WHITE);
        southPanel.add(tipLabel, BorderLayout.NORTH);
        southPanel.add(goMainButton, BorderLayout.SOUTH);

        // 레이아웃 구성
        setLayout(new BorderLayout());
        add(titleLabel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }
    
//    // 실행 테스트
//    public static void main(String[] args) {
//    	User u = new User("test", "123", "080322");
//        new RankView(u).setVisible(true);
//    }
}
