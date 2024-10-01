package Game.card;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

public class CardUI extends JPanel {
    private JLabel healthLabel;
    private JLabel attackLabel;
    private JLabel nameLabel;
    private JLabel tagLabel;
    private Font customFont;

    public CardUI(Card card) {
        setLayout(null);  // 절대 위치 레이아웃

        // 사용자 지정 폰트 로드
        loadCustomFont();

        // 이미지 경로를 클래스패스에서 불러오기
        ImageIcon cardImage = new ImageIcon(getClass().getResource(card.getImage()));
        JLabel cardImageLabel = new JLabel(cardImage);
        cardImageLabel.setBounds(0, 0, cardImage.getIconWidth(), cardImage.getIconHeight());  // 이미지 크기에 맞게 설정
        add(cardImageLabel);  // 카드 이미지 추가

        // 카드 이름 (상단 중앙)
        nameLabel = new JLabel(card.getName(), SwingConstants.CENTER);
        nameLabel.setForeground(Color.BLACK);
        nameLabel.setFont(customFont.deriveFont(18f));  // 사용자 지정 폰트 설정
        nameLabel.setBounds(cardImage.getIconWidth() / 2 - 50, 10, 100, 20);  // 상단 중앙에 배치
        add(nameLabel);

        // 카드 태그 (중앙 아래)
        tagLabel = new JLabel(card.getTag(), SwingConstants.CENTER);
        tagLabel.setForeground(Color.BLACK);
        tagLabel.setFont(customFont.deriveFont(14f));  // 사용자 지정 폰트 설정
        tagLabel.setBounds(cardImage.getIconWidth() / 2 - 50, cardImage.getIconHeight() / 2 + 40, 100, 20);  // 중앙 아래에 배치
        add(tagLabel);

        // 카드 공격력 (왼쪽 하단)
        attackLabel = new JLabel("" + card.getAttack(), SwingConstants.LEFT);
        attackLabel.setForeground(Color.BLACK);  // 텍스트 색상 설정
        attackLabel.setFont(customFont.deriveFont(16f));  // 사용자 지정 폰트 설정
        attackLabel.setBounds(10, cardImage.getIconHeight() - 40, 50, 30);  // 왼쪽 하단에 배치
        add(attackLabel);

        // 카드 체력 (오른쪽 하단)
        healthLabel = new JLabel("" + card.getHealth(), SwingConstants.RIGHT);
        healthLabel.setForeground(Color.BLACK);  // 텍스트 색상 설정
        healthLabel.setFont(customFont.deriveFont(16f));  // 사용자 지정 폰트 설정
        healthLabel.setBounds(cardImage.getIconWidth() - 60, cardImage.getIconHeight() - 40, 50, 30);  // 오른쪽 하단에 배치
        add(healthLabel);
    }

    // 폰트를 불러오는 메서드
    private void loadCustomFont() {
        try {
            InputStream fontStream = getClass().getResourceAsStream("/resources/fonts/Galmuri7.ttf");
            if (fontStream != null) {
                customFont = Font.createFont(Font.TRUETYPE_FONT, fontStream).deriveFont(24f);
            } else {
                System.out.println("폰트 파일을 찾을 수 없습니다.");
            }
        } catch (FontFormatException e) {
            e.printStackTrace(); // 폰트 포맷 오류 처리
        } catch (IOException e) {
            e.printStackTrace(); // 입출력 오류 처리
        }
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
