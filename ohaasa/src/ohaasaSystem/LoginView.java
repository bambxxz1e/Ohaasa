package ohaasaSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import model.User;

public class LoginView extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JTextField birthField;
    private JButton signupButton;
    private JButton loginButton;
    private DBManager dbmanager;
    
    public LoginView() {
    	this.dbmanager = new DBManager();
        initializeComponents();
        setupLayout();
        setupEventHandlers();
        
        // 창 설정
        setTitle("회원가입/로그인");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(350, 290);
        setLocationRelativeTo(null); // 화면 중앙에 위치
        setResizable(false);
        setVisible(true);
    }
    
    private void initializeComponents() {
        usernameField = new JTextField(15);
        passwordField = new JPasswordField(15);
        birthField = new JTextField("YYMMDD", 15);
        signupButton = new JButton("회원가입");
        loginButton = new JButton("로그인");
        
        // 버튼 스타일 설정
        signupButton.setBackground(new Color(70, 130, 180));
        signupButton.setForeground(Color.WHITE);
        signupButton.setFocusPainted(false);
        
        loginButton.setBackground(new Color(70, 130, 180));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        
        birthField.setForeground(Color.GRAY); // placeholder 역할
        birthField.addFocusListener(new FocusAdapter() {
            @Override
            public void focusGained(FocusEvent e) {
                if (birthField.getText().equals("YYMMDD")) {
                    birthField.setText("");
                    birthField.setForeground(Color.BLACK);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (birthField.getText().isEmpty()) {
                    birthField.setText("YYMMDD");
                    birthField.setForeground(Color.GRAY);
                }
            }
        });
    }
    
    private void setupLayout() {
        setLayout(new BorderLayout());
        
        // 메인 패널
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(30, 40, 30, 40));
        mainPanel.setBackground(Color.WHITE);
        
        // 제목 라벨
        JLabel titleLabel = new JLabel("시스템 로그인", SwingConstants.CENTER);
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // 사용자명 패널
        JPanel usernamePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        usernamePanel.setBackground(Color.WHITE);
        JLabel usernameLabel = new JLabel("아이디 :");
        usernameLabel.setPreferredSize(new Dimension(80, 25));
        usernamePanel.add(usernameLabel);
        usernamePanel.add(usernameField);
        
        // 비밀번호 패널
        JPanel passwordPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        passwordPanel.setBackground(Color.WHITE);
        JLabel passwordLabel = new JLabel("비밀번호 :");
        passwordLabel.setPreferredSize(new Dimension(80, 25));
        passwordPanel.add(passwordLabel);
        passwordPanel.add(passwordField);
        
        // 생년월일 패널
        JPanel birthPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        birthPanel.setBackground(Color.WHITE);
        JLabel birthLabel = new JLabel("생년월일 :");
        birthLabel.setPreferredSize(new Dimension(80, 25));
        birthPanel.add(birthLabel);
        birthPanel.add(birthField);
        
        // 버튼 패널
        JPanel buttonPanel = new JPanel(new FlowLayout());
        buttonPanel.setBackground(Color.WHITE);
        buttonPanel.add(signupButton);
        buttonPanel.add(loginButton);
        
        // 컴포넌트들을 메인 패널에 추가
        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(20)); // 간격
        mainPanel.add(usernamePanel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(passwordPanel);
        mainPanel.add(Box.createVerticalStrut(8));
        mainPanel.add(birthPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(buttonPanel);
        
        add(mainPanel, BorderLayout.CENTER);
    }
    
    private void setupEventHandlers() {
    	// 회원가입 버튼 이벤트
        signupButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSignUp();
            }
        });
    	
        // 로그인 버튼 이벤트
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        
        // Enter 키로 로그인 실행
        passwordField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleLogin();
            }
        });
        
        // 사용자명 필드에서 Enter 키 누르면 비밀번호 필드로 이동
        usernameField.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                passwordField.requestFocus();
            }
        });
    }
    
    private void handleSignUp() { // 회원가입 관리
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String birth = birthField.getText().trim();
        
        // 입력값 검증
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "사용자명을 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
            usernameField.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "비밀번호를 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
            passwordField.requestFocus();
            return;
        }
        
        if (birth.isEmpty()) {
            JOptionPane.showMessageDialog(this, "생년월일을 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
            passwordField.requestFocus();
            return;
        }
        else if(birth.length() != 6) {
        	JOptionPane.showMessageDialog(this, "생년월일은 6자리로 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
            passwordField.requestFocus();
            return;
        }
        
        // 회원가입 처리
        if (dbmanager.registerUser(username, password, birth)) {
            JOptionPane.showMessageDialog(this, "회원가입 성공!", "성공", JOptionPane.INFORMATION_MESSAGE);
            // 필드 초기화
            usernameField.setText("");
            passwordField.setText("");
            birthField.setText("YYMMDD");
            birthField.setForeground(Color.GRAY);
            usernameField.requestFocus();
        } else {
            JOptionPane.showMessageDialog(this, "회원가입 중 오류가 발생했습니다.", "회원가입 실패", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void handleLogin() { // 로그인 관리
        String username = usernameField.getText().trim();
        String password = new String(passwordField.getPassword());
        String birth = birthField.getText().trim();
        
        // 입력값 검증
        if (username.isEmpty()) {
            JOptionPane.showMessageDialog(this, "사용자명을 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
            usernameField.requestFocus();
            return;
        }
        
        if (password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "비밀번호를 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
            passwordField.requestFocus();
            return;
        }
        
        if (birth.isEmpty()) {
            JOptionPane.showMessageDialog(this, "생년월일을 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
            passwordField.requestFocus();
            return;
        }
        else if(birth.length() != 6) {
        	JOptionPane.showMessageDialog(this, "생년월일은 6자리로 입력해주세요.", "입력 오류", JOptionPane.WARNING_MESSAGE);
            passwordField.requestFocus();
            return;
        }
        
        // 로그인 검증
        if (dbmanager.validateLogin(username, password, birth)) {
            // 사용자 정보 가져오기
            User currentUser = dbmanager.getUserInfo(username);
            
            JOptionPane.showMessageDialog(this, 
                "로그인 성공!\n별자리: " + currentUser.getZodiacSign(), 
                "성공", JOptionPane.INFORMATION_MESSAGE);
            
            dispose(); // 로그인 창 닫기
            // new MainView(currentUser); // 메인 화면 호출 (사용자 정보 전달)
        } else {
            JOptionPane.showMessageDialog(this, "입력 정보가 잘못되었습니다.", "로그인 실패", JOptionPane.ERROR_MESSAGE);
            passwordField.setText(""); // 비밀번호 필드 초기화
            usernameField.requestFocus();
        }
    }
}