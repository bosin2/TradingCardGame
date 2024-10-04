package Game.view;

import javax.swing.JPanel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import Game.control.Control;

public class view_main extends JPanel {

    private MainFrame mainFrame;
    private JButton button_1;
    private JButton button_2;
    private JButton button_3;
    private Image backgroundImage; // 배경 이미지 변수 추가

    public view_main(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        backgroundImage = new ImageIcon(getClass().getResource("/resources/background/mainmenu.png")).getImage();

        // 레이아웃 설정 및 컴포넌트 추가
        setLayout(null);
        setPreferredSize(new Dimension(1300, 800));

        button_1 = createImageButton("/resources/icon/start_Button.png");
        button_1.setBounds(830, 250, 200, 100);
        add(button_1);

        button_2 = createImageButton("/resources/icon/continue_Button.png");
        button_2.setBounds(830, 450, 200, 100);
        add(button_2);

        button_3 = createImageButton("/resources/icon/manual_Button.png");
        button_3.setBounds(830, 650, 200, 100);
        add(button_3);

        // 컨트롤러 초기화
        Control controller = new Control(this, mainFrame);
        controller.initController();
    }
    
 // 이미지 버튼 생성 메서드
    private JButton createImageButton(String imagePath) {
        // 이미지 불러오기
        ImageIcon icon = new ImageIcon(getClass().getResource(imagePath));
        JButton button = new JButton(icon);

        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setContentAreaFilled(false);

        return button;
    }
    
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g); 
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }



    // 버튼 반환 메서드들
    public JButton getButton1() {
        return button_1;
    }

    public JButton getButton2() {
        return button_2;
    }

    public JButton getButton3() {
        return button_3;
    }
}
