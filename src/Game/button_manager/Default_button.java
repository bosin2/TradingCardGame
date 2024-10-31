package Game.button_manager;

import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.io.InputStream;

import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Default_button {

    // 이미지 경로를 상수로 정의
    private static final String IMAGE_PATH = "/resources/icon/standard_Button.png";

    // 사용자 지정 폰트를 저장할 변수
    private static Font customFont;

    // 정적 초기화 블록을 사용하여 폰트를 한 번만 불러온
    static {
        try {
            InputStream fontStream = Default_button.class.getResourceAsStream("/resources/fonts/Galmuri7.ttf");
            if (fontStream != null) {
                customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(25f);
                System.out.println("폰트 로드 성공");
            } else {
                System.out.println("폰트 파일을 찾을 수 없습니다. 기본 폰트를 사용합니다.");
                customFont = new Font("Serif", Font.BOLD, 25);
            }
        } catch (Exception e) {
            e.printStackTrace();
            customFont = new Font("Serif", Font.BOLD, 25);
        }
    }

    // 버튼 생성 메서드
    public JButton createImageButton(String buttonText) {
        // 이미지 불러오기
        ImageIcon icon = new ImageIcon(getClass().getResource(IMAGE_PATH));
        Image image = icon.getImage().getScaledInstance(200, 100, Image.SCALE_SMOOTH); // 크기 조정
        ImageIcon resizedIcon = new ImageIcon(image);

        // 버튼 생성 및 이미지 설정
        JButton button = new JButton(buttonText, resizedIcon);
        button.setHorizontalTextPosition(JButton.CENTER); // 텍스트의 수평 위치를 가운데로 설정
        button.setVerticalTextPosition(JButton.CENTER); // 텍스트의 수직 위치를 가운데로 설정

        // 버튼 외형 설정 (기본 배경 및 테두리 제거)
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setForeground(Color.BLACK); // 텍스트 색상 설정
        button.setFont(customFont); // 불러온 폰트를 사용

        return button;
    }
}
