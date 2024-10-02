package Game.view;

import javax.swing.JPanel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class view_manual extends JPanel {

    private MainFrame mainFrame;

    public view_manual(MainFrame mainFrame) {
        this.mainFrame = mainFrame;

        // 패널 초기화 및 컴포넌트 추가
        setLayout(null);

        // 예시로 뒤로 가기 버튼 추가
        JButton backButton = new JButton("뒤로");
        backButton.setBounds(600, 700, 100, 50);
        add(backButton);

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.showView("main"); // 메인 화면으로 전환
            }
        });
    }
}
