package Game.card;

import javax.swing.*;
import java.awt.*;

public class CardUI extends JPanel {
    private JLabel healthLabel;
    private JLabel attackLabel;

    public CardUI(String imagePath, int health, int attack) {
        setLayout(new BorderLayout());

        // 카드 이미지 설정
        ImageIcon cardImage = new ImageIcon(imagePath);
        JLabel cardImageLabel = new JLabel(cardImage);

        // 카드 정보 레이아웃 설정 (텍스트 오버레이)
        JLayeredPane layeredPane = new JLayeredPane();
        layeredPane.setPreferredSize(new Dimension(cardImage.getIconWidth(), cardImage.getIconHeight()));

        // 카드 이미지 추가
        cardImageLabel.setBounds(0, 0, cardImage.getIconWidth(), cardImage.getIconHeight());
        layeredPane.add(cardImageLabel, Integer.valueOf(0)); // 레이어 0에 이미지 추가

        // 카드 공격력 및 체력 텍스트 추가
        attackLabel = new JLabel("" + attack);
        attackLabel.setForeground(Color.WHITE);  // 텍스트 색상 설정
        attackLabel.setBounds(10, 10, 100, 20);  // 공격력 텍스트 위치
        layeredPane.add(attackLabel, Integer.valueOf(1)); // 레이어 1에 텍스트 추가

        healthLabel = new JLabel("" + health);
        healthLabel.setForeground(Color.WHITE);
        healthLabel.setBounds(10, 40, 100, 20); // 체력 텍스트 위치
        layeredPane.add(healthLabel, Integer.valueOf(1));

        add(layeredPane, BorderLayout.CENTER);
    }

    // 체력이 변할 때 호출되는 메서드
    public void updateHealth(int newHealth) {
        healthLabel.setText("" + newHealth);
    }

    // 공격력이 변할 때 호출되는 메서드
    public void updateAttack(int newAttack) {
        attackLabel.setText("" + newAttack);
    }
}
