package Game.view;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import javax.swing.JPanel;

public class MainFrame extends JFrame {

    private CardLayout cardLayout;
    private JPanel mainPanel;

    public MainFrame() {
        setTitle("교수님! A 주세요! 혹시 +도?!");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1300, 800);
        setResizable(false);
        
        setLayout(new BorderLayout());

        // CardLayout을 사용하는 메인 패널 생성
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        // 각 뷰 생성
        view_main mainView = new view_main(this);
        start_intro introView = new start_intro(this);
        view_manual manualView = new view_manual(this);
        view_fight fightView = new view_fight(this);

        // 메인 패널에 각 뷰 추가
        mainPanel.add(mainView, "main");
        mainPanel.add(introView, "intro");
        mainPanel.add(manualView, "manual");
        mainPanel.add(fightView, "fight");

        // 메인 패널을 프레임에 추가
        add(mainPanel);

        setVisible(true);
    }

    // 화면 전환 메서드
    public void showView(String viewName) {
        cardLayout.show(mainPanel, viewName);
    }

    public static void main(String[] args) {
        // 이벤트 디스패치 스레드에서 실행
        SwingUtilities.invokeLater(() -> {
            new MainFrame();
        });
    }
}
