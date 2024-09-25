package Game.view;

import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.CardLayout;

public class view_manual extends JPanel {

    private static final long serialVersionUID = 1L;

    /**
     * Create the panel.
     */
    public view_manual() {
        setLayout(null);
        
        JLabel label = new JLabel("이곳은 설명서 화면입니다.");
        label.setBounds(100, 100, 200, 50);
        add(label);

        // 뒤로가기 버튼 추가
        JButton backButton = new JButton("뒤로가기");
        backButton.setBounds(100, 200, 100, 50);
        add(backButton);

        // 뒤로가기 버튼 클릭 시 메인 화면으로 돌아가는 기능 추가
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JPanel parent = (JPanel) getParent();
                CardLayout cardLayout = (CardLayout) parent.getLayout();
                cardLayout.show(parent, "mainPanel");  // 메인 화면으로 전환
            }
        });
    }
}
