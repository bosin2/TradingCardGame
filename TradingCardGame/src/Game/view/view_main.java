package Game.view;

import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import Game.control.Control;
import javax.swing.JLayeredPane;
import java.awt.Button;
import java.awt.CardLayout;

public class view_main extends JFrame {

    private static final long serialVersionUID = 1L;
    
    private JPanel contentPane;
    private JPanel cardPanel;  // 패널을 담을 컨테이너
    private Button button_1;
    private Button button_2;
    private Button button_3;

    /**
     * Launch the application.
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    view_main frame = new view_main();
                    frame.setVisible(true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * Create the frame.
     */
    public view_main() {
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(100, 100, 1300, 800);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setLayout(null);  // null 레이아웃으로 설정 (절대 위치 사용 가능)
        contentPane.setBorder(new EmptyBorder(10, 10, 10, 10));
        setContentPane(contentPane);

        // CardLayout 패널 설정
        cardPanel = new JPanel(new CardLayout());
        cardPanel.setBounds(0, 0, 1300, 800);
        contentPane.add(cardPanel);
        
        // 메인 화면 패널 생성
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);

        button_1 = new Button("시작하기");
        button_1.setBounds(530, 450, 187, 51);
        mainPanel.add(button_1);
        
        button_2 = new Button("이어하기");
        button_2.setBounds(530, 550, 187, 51);
        mainPanel.add(button_2);
        
        button_3 = new Button("설명서");
        button_3.setBounds(530, 650, 187, 51);
        mainPanel.add(button_3);
        
        // 메인 패널과 설명서 패널을 cardPanel에 추가
        cardPanel.add(mainPanel, "mainPanel");
        cardPanel.add(new view_manual(), "manual");
        cardPanel.add(new start_intro(),"startintro");

        // 컨트롤러 초기화
        Control controller = new Control(this);
        controller.initController();
    }

    // button_1 반환 메서드
    public Button getButton1() {
        return button_1;
    }

    // button_2 반환 메서드
    public Button getButton2() {
        return button_2;
    }

    // button_3 반환 메서드
    public Button getButton3() {
        return button_3;
    }
    
    // CardLayout 패널 반환 메서드
    public JPanel getCardPanel() {
        return cardPanel;
    }
    
    // 설명서 화면으로 전환하는 메서드
    public void showManualView() {
        CardLayout cl = (CardLayout)(cardPanel.getLayout());
        cl.show(cardPanel, "manual");
        cardPanel.revalidate();
        cardPanel.repaint();
    }
    public void showstartintroView() {
        CardLayout cl = (CardLayout)(cardPanel.getLayout());
        cl.show(cardPanel, "startintro");
        cardPanel.revalidate();
        cardPanel.repaint();
    }
}
