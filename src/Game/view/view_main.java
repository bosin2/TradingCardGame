package Game.view;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.CardLayout;
import java.awt.Dimension;
import Game.control.Control;

public class view_main extends JPanel {

    private MainFrame mainFrame;
    private JButton button_1;
    private JButton button_2;
    private JButton button_3;

    public view_main(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        // 레이아웃 설정 및 컴포넌트 추가
        setLayout(null);
        setPreferredSize(new Dimension(1300, 800));

        button_1 = new JButton("시작하기");
        button_1.setBounds(530, 450, 187, 51);
        add(button_1);

        button_2 = new JButton("이어하기");
        button_2.setBounds(530, 550, 187, 51);
        add(button_2);

        button_3 = new JButton("설명서");
        button_3.setBounds(530, 650, 187, 51);
        add(button_3);

        // 컨트롤러 초기화
        Control controller = new Control(this, mainFrame);
        controller.initController();
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
