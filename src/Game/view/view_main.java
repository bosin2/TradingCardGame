package Game.view;

import javax.swing.JPanel;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import Game.button_manager.Default_button;
import Game.button_manager.Mainmenu_button;

public class view_main extends JPanel {

    private MainFrame mainFrame;
    private JButton button_1;
    private JButton button_2;
    private JButton button_3;
    private Image backgroundImage; // 배경 이미지 변수 추가

    public view_main(MainFrame mainFrame) {
        this.mainFrame = mainFrame;
        Default_button buttonManager = new Default_button();

        backgroundImage = new ImageIcon(getClass().getResource("/resources/background/mainmenu.png")).getImage();

        // 레이아웃 설정 및 컴포넌트 추가
        setLayout(null);
        setPreferredSize(new Dimension(1300, 800));

        button_1 = buttonManager.createImageButton("시작하기");
        button_1.setBounds(900, 150, 200, 100);
        add(button_1);
        
        button_2 = buttonManager.createImageButton("이어하기");
        button_2.setBounds(900, 350, 200, 100);
        add(button_2);

        button_3 = buttonManager.createImageButton("설명서");
        button_3.setBounds(900, 550, 200, 100);
        add(button_3);

        // 컨트롤러 초기화
        Mainmenu_button controller = new Mainmenu_button(this, mainFrame);
        controller.initController();
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
