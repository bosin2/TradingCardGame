package Game.button_manager;
import java.awt.Color;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.InputStream;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class Default_button {

    private static final String IMAGE_PATH = "/resources/icon/standard_Button.png";
    private static Font customFont;

    static {
        try {
            InputStream fontStream = Default_button.class.getResourceAsStream("/resources/fonts/Galmuri7.ttf");
            if (fontStream != null) {
                customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(28f);
                System.out.println("폰트 로드 성공");
            } else {
                System.out.println("폰트 파일을 찾을 수 없습니다. 기본 폰트를 사용합니다.");
                customFont = new Font("Serif", Font.BOLD, 28);
            }
        } catch (Exception e) {
            e.printStackTrace();
            customFont = new Font("Serif", Font.BOLD, 28);
        }
    }

    public JButton createImageButton(String buttonText) {
        ImageIcon icon = new ImageIcon(getClass().getResource(IMAGE_PATH));
        Image image = icon.getImage().getScaledInstance(200, 100, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(image);

        JButton button = new JButton(buttonText, resizedIcon);
        button.setHorizontalTextPosition(JButton.CENTER);
        button.setVerticalTextPosition(JButton.CENTER);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);
        button.setForeground(Color.WHITE);
        button.setFont(customFont);
        
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                Image scaledImage = icon.getImage().getScaledInstance(180, 90, Image.SCALE_SMOOTH); // 아이콘 크기 줄이기
                button.setIcon(new ImageIcon(scaledImage));
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setIcon(resizedIcon); // 원래 아이콘 크기로 복원
            }
        });

        return button;
    }
}
